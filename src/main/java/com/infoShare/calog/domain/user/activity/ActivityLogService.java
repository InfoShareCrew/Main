package com.infoShare.calog.domain.user.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActivityLogService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getActivityLogsByUserId(Long userId, String sortType) {
        String sql = "SELECT a.title AS articleTitle, c.name AS cafeName, a.view, a.content, a.id AS articleId, c.id AS cafeId, " +
                "COALESCE(av.voter, 0) AS voter, COALESCE(co.comment, 0) AS comment " +
                "FROM article a " +
                "JOIN cafe c ON a.cafe = c.id " +
                "LEFT JOIN (SELECT article_id, COUNT(*) AS voter FROM article_voter GROUP BY article_id) av ON av.article_id = a.id " +
                "LEFT JOIN (SELECT article_id, COUNT(*) AS comment FROM comment GROUP BY article_id) co ON co.article_id = a.id " +
                "JOIN Site_User u ON a.author_id = u.id " +
                "WHERE u.id = ? " +
                "GROUP BY a.id ";

        // 정렬 방식에 따라 쿼리 수정
        if ("popular".equals(sortType)) {
            sql += "ORDER BY a.view DESC"; // 조회수 기준으로 정렬
        } else {
            sql += "ORDER BY MAX(a.created_date) DESC"; // 최신순
        }

        return jdbcTemplate.queryForList(sql, userId);
    }

    public List<Map<String, Object>> getActivityCafesByUserId(Long userId) {
        String sql = "SELECT c.name AS name, c.intro AS intro, c.category_id AS category, c.id AS cafeId " +
                "FROM cafe c " +
                "JOIN Site_User u ON c.manager_id = u.id " +
                "WHERE u.id = ? " +
                "GROUP BY c.name, c.intro, c.category_id " +
                "ORDER BY c.created_date DESC";

        return jdbcTemplate.queryForList(sql, userId);
    }
}
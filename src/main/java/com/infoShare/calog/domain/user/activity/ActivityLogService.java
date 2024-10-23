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

    public List<Map<String, Object>> getActivityLogsByUserId(Long userId) {
        String sql = "SELECT a.title AS articleTitle, c.name AS cafeName, a.view, a.content, a.id AS articleId, c.id AS cafeId, COUNT(DISTINCT av.article_id) AS voter, COUNT(DISTINCT co.article_id) AS comment " +
                "FROM article a " +
                "JOIN cafe c " + // assuming a.cafe_id exists
                "LEFT JOIN article_voter av ON av.article_id " +
                "LEFT JOIN comment co ON co.article_id " +
                "JOIN Site_User u ON a.author_id = u.id " +  // assuming this relationship
                "WHERE u.id = ? " +
                "GROUP BY a.id " +
                "ORDER BY MAX(a.created_date) DESC";

        return jdbcTemplate.queryForList(sql, userId);
    }

    public List<Map<String, Object>> getActivityCafesByUserId(Long userId) {
        String sql = "SELECT c.name AS name, c.intro AS intro, c.category_id AS category, c.id AS cafeId " +
                "FROM cafe c " +
                "JOIN Site_User u ON c.maneger_id = u.id " +
                "WHERE u.id = ? " +
                "GROUP BY c.name, c.intro, c.category_id " +
                "ORDER BY c.created_date DESC";

        return jdbcTemplate.queryForList(sql, userId);
    }
}
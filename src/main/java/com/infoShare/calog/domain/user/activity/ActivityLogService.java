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
        String sql = "SELECT a.title AS articleTitle, c.name AS cafeName, a.view, a.content, a.id AS articleId, c.id AS cafeId, COUNT(av.article_id) AS voter, COUNT(co.article_id) AS comment " +
                "FROM article a " +
                "JOIN cafe c " + // assuming a.cafe_id exists
                "LEFT JOIN article_voter av ON av.article_id = a.id " +
                "LEFT JOIN comment co ON co.article_id = a.id " +
                "JOIN Site_User u ON c.id = u.id " +  // assuming this relationship
                "WHERE u.id = ? " +
                "GROUP BY a.title, c.name, a.view, a.content, a.id, c.id " +
                "ORDER BY MAX(a.created_date) DESC";;

        return jdbcTemplate.queryForList(sql, userId);
    }
}
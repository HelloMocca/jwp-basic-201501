package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import next.model.Answer;
import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;

public class AnswerDao {
	private static AnswerDao instance = new AnswerDao();
	
	public static AnswerDao getInstance() {
		return instance;
	}
	
	
	public void insert(Answer answer) {
		JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();
		String sql = "INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, answer.getWriter(),
				answer.getContents(),
				new Timestamp(answer.getTimeFromCreateDate()),
				answer.getQuestionId());
		sql = "UPDATE QUESTIONS SET countOfComment = countOfComment + 1 where questionId = ?";
		jdbcTemplate.update(sql, answer.getQuestionId());
	}
	
	public void delete(Answer answer) {
		JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();
		String sql = "DELETE FROM ANSWERS WHERE answerId = ?";
		jdbcTemplate.update(sql, answer.getAnswerId());
		sql = "UPDATE QUESTIONS SET countOfComment = countOfComment - 1 where questionId = ?";
		jdbcTemplate.update(sql, answer.getQuestionId());
	}

	public List<Answer> findAllByQuestionId(long questionId) {
		JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();
		String sql = "SELECT answerId, writer, contents, createdDate FROM ANSWERS WHERE questionId = ? "
				+ "order by answerId desc";
		
		RowMapper<Answer> rm = new RowMapper<Answer>() {
			@Override
			public Answer mapRow(ResultSet rs) throws SQLException {
				return new Answer(
						rs.getLong("answerId"),
						rs.getString("writer"), 
						rs.getString("contents"),
						rs.getTimestamp("createdDate"), 
						questionId);
			}
		};
		
		return jdbcTemplate.query(sql, rm, questionId);
	}
	
	private AnswerDao() { }
}

package ureca.muneobe.common.vector.repository;

import com.pgvector.PGvector;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.vector.entity.Fat;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FatJdbcRepository {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public void updateEmbedding(Long id, float[] vector) {
        String sql = "UPDATE fat SET embedding = ? WHERE id = ?";
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            PGvector.registerTypes(conn);
            stmt.setObject(1, new PGvector(vector));
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("벡터 업데이트 실패", e);
        }
    }

    public void insertEmbedding(Long id, float[] vector) {
        String sql = "INSERT INTO fat_embedding (fat_id, embedding) VALUES (?, ?)";
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            PGvector.registerTypes(conn);
            stmt.setLong(1, id);
            stmt.setObject(2, new PGvector(vector));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("벡터 업데이트 실패", e);
        }
    }



    /**
     * 유사한 요금제를 벡터 기반으로 검색
     *
     * @param queryVector 벡터화된 사용자 쿼리
     * @param limit       검색 결과 개수
     * @return 유사 요금제 리스트
     */

    private final RowMapper<Fat> fatRowMapper = (rs, rowNum) -> {
        Fat plan = new Fat();
        plan.setId(rs.getLong("id"));
        plan.setTextMessages(rs.getObject("text_messages") != null ? rs.getBoolean("text_messages") : null);
        plan.setBasicDataAmount(rs.getObject("basic_data_amount") != null ? rs.getLong("basic_data_amount") : null);
        plan.setDailyData(rs.getObject("daily_data") != null ? rs.getLong("daily_data") : null);
        plan.setMonthlyPrice(rs.getObject("monthly_price") != null ? rs.getLong("monthly_price") : null);
        plan.setPrice(rs.getObject("price") != null ? rs.getLong("price") : null);
        plan.setSharingData(rs.getObject("sharing_data") != null ? rs.getLong("sharing_data") : null);
        plan.setSubDataSpeed(rs.getObject("sub_data_speed") != null ? rs.getLong("sub_data_speed") : null);
        plan.setVoiceCallVolume(rs.getObject("voice_call_volume") != null ? rs.getLong("voice_call_volume") : null);
        plan.setAddon(rs.getString("addon"));
        plan.setDescription(rs.getString("description"));
        plan.setName(rs.getString("name"));
        plan.setDataType(rs.getString("data_type"));
        plan.setMplanType(rs.getString("mplan_type"));
        plan.setQualification(rs.getString("qualification"));
        plan.setEmbedding(rs.getObject("is_embedding") != null ? rs.getBoolean("is_embedding") : false);
        return plan;
    };


    public List<Fat> findSimilarPlans(float[] queryVector, int limit) {
        try (Connection conn = dataSource.getConnection()) {
            PGvector.registerTypes(conn); // 벡터 타입 등록

            String sql = """
                        SELECT f.*
                                            FROM (
                                                SELECT DISTINCT ON (fe.fat_id) fe.fat_id, fe.embedding
                                                FROM fat_embedding fe
                                                ORDER BY fe.fat_id, fe.embedding <=> ?
                                            ) distinct_fe
                                            JOIN fat f ON f.id = distinct_fe.fat_id
                                            ORDER BY distinct_fe.embedding <=> ?
                                            LIMIT ?;
                    """;

            return jdbcTemplate.query(
                    sql,
                    ps -> {
                        ps.setObject(1, new PGvector(queryVector));
                        ps.setObject(2, new PGvector(queryVector));
                        ps.setInt(3, limit);
                    },
                    fatRowMapper
            );
        } catch (SQLException e) {
            throw new RuntimeException("유사 요금제 검색 실패", e);
        }
    }
}
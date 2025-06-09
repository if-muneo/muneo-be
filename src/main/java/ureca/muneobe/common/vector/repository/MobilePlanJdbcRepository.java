package ureca.muneobe.common.vector.repository;

import com.pgvector.PGvector;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ureca.muneobe.common.vector.entity.MobilePlan;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MobilePlanJdbcRepository {
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
            throw new RuntimeException("❌ 벡터 업데이트 실패", e);
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

    private final RowMapper<MobilePlan> mobilePlanRowMapper = (rs, rowNum) -> {
        MobilePlan plan = new MobilePlan();
        plan.setId(rs.getLong("id"));
        plan.setTextMessages(rs.getBoolean("text_messages"));
        plan.setBasicDataAmount(rs.getLong("basic_data_amount"));
        plan.setDailyData(rs.getLong("daily_data"));
        plan.setMonthlyPrice(rs.getLong("monthly_price"));
        plan.setPrice(rs.getLong("price"));
        plan.setSharingData(rs.getLong("sharing_data"));
        plan.setSubDataSpeed(rs.getLong("sub_data_speed"));
        plan.setVoiceCallVolume(rs.getLong("voice_call_volume"));
        plan.setAddon(rs.getString("addon"));
        plan.setDataType(MobilePlan.DataType.valueOf(rs.getString("data_type")));
        plan.setDescription(rs.getString("description"));
        plan.setMplanType(MobilePlan.MPlanType.valueOf(rs.getString("mplan_type")));
        plan.setName(rs.getString("name"));
        plan.setQualification(MobilePlan.Qualification.valueOf(rs.getString("qualification")));
        return plan;
    };

    /**
     * 유사한 요금제를 벡터 기반으로 검색
     *
     * @param queryVector 벡터화된 사용자 쿼리
     * @param limit       검색 결과 개수
     * @return 유사 요금제 리스트
     */

    public List<MobilePlan> findSimilarPlans(float[] queryVector, int limit) {
        try (Connection conn = dataSource.getConnection()) {
            PGvector.registerTypes(conn); // 벡터 타입 등록

            String sql = """
                        SELECT f.*
                        FROM fat_embedding fe
                        JOIN fat f ON fe.fat_id = f.id
                        ORDER BY fe.embedding <=> ?
                        LIMIT ?
                    """;

            return jdbcTemplate.query(
                    sql,
                    ps -> {
                        ps.setObject(1, new PGvector(queryVector));
                        ps.setInt(2, limit);
                    },
                    mobilePlanRowMapper
            );

        } catch (SQLException e) {
            throw new RuntimeException("유사 요금제 검색 실패", e);

        }
    }
}
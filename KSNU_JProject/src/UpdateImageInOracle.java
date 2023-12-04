import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateImageInOracle {
    public static void main(String[] args) {
        // JDBC 연결 정보
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "C##jaehun";
        String password = "1234";

        // 이미지 파일 경로
        String imagePath = "images/무령왕비 은팔찌1.jpg";

        // 업데이트할 문화재순번
        int culturalHeritageNumber = 8; // 예시로 1로 설정

        // 데이터베이스 연결
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // SQL 쿼리 작성
            String sql = "UPDATE CULTURE_HERITAGE SET 이미지2 = ? WHERE 문화재순번 = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // 이미지 파일을 바이트 배열로 읽어오기
                File imageFile = new File(imagePath);
                try (InputStream inputStream = new FileInputStream(imageFile)) {
                    // 쿼리 매개변수 설정
                    preparedStatement.setBinaryStream(1, inputStream, (int) imageFile.length());
                    preparedStatement.setInt(2, culturalHeritageNumber);

                    // 쿼리 실행
                    preparedStatement.executeUpdate();
                }
            }

            System.out.println("Image updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

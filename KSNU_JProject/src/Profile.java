import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.nio.channels.FileChannel;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.nio.file.Files;
import javax.imageio.ImageIO;

public class Profile extends JPanel {
    static final String driver = "oracle.jdbc.OracleDriver";
    static final String url = "jdbc:oracle:thin:@localhost:1521:xe";
    static final String id = "C##jaehun";
    static final String pw = "1234";
    private JTextArea 문화재순번;
    private JTextArea 문화재순번1;
    private JTextArea 문화재명;
    private JTextArea 문화재명1;
    private JTextArea 재정일자;
    private JTextArea 재정일자1;
    private JTextArea 주소;
    private JTextArea 주소1;
    private JLabelWithBackground imageLabel;
    private JButton addinformation;
    private JButton image;
    private Profile profilePanel;
    
    
    String image1;
   
    int x = 100;

    public Profile(String 문화재순번1) {
        setBackground(Color.white);
        setLayout(null);

        add(createLabel("문화재순번:", 270, 700, 100, 25));
        add(createLabel("문화재명:", 270, 735, 100, 25));
        add(createLabel("재정일자:", 270, 770, 100, 25));
        add(createLabel("주소:", 270, 805, 100, 25));

        try {
            // 기존 텍스트 영역을 사용하도록 수정
            문화재순번 = createTextArea("", 400, 700, 200, 25);
            문화재명 = createTextArea("", 400, 735, 200, 25);
            재정일자 = createTextArea("", 400, 770, 200, 25);
            주소 = createTextArea("", 400, 805, 200, 25);

            add(문화재순번);
            add(문화재명);
            add(재정일자);
            add(주소);

            // 데이터베이스에서 값 가져오기
            String 문화재순번FromDB = getDatanumber(문화재순번1);
            String 문화재명FromDB = getDataname(문화재순번1);
            String 재정일자FromDB = getDatadate(문화재순번1);
            String 주소FromDB = getDataaddress(문화재순번1);

            // 가져온 값들을 JTextArea에 설정
            문화재순번.setText(문화재순번FromDB);
            문화재명.setText(문화재명FromDB);
            재정일자.setText(재정일자FromDB);
            주소.setText(주소FromDB);

            byte[] imageData = getImageDataByNumber(문화재순번1);
            ImageIcon imageIcon = new ImageIcon(imageData);
            JLabel imageLabel = new JLabel(imageIcon);
            this.add(imageLabel);
            
            BufferedImage image = getImageFromDatabase(문화재순번1);
            if (image != null) {
                // JLabelWithBackground를 사용하여 이미지를 출력
                imageLabel = new JLabelWithBackground(image);
                imageLabel.setBounds(200, 200, 400, 400);
                add(imageLabel);
                revalidate();  // 패널 다시 그리기
                repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 이미지 버튼을 생성하고 패널에 추가
        image = imageButton(image1, 200, 200, 400, 400);
        add(image);
        
        
    }
    private byte[] getImageDataByNumber(String 문화재순번1) {
        byte[] imageData = null;

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "C##jaehun", "1234")) {
            String query = "SELECT 이미지2 FROM CULTURE_HERITAGE WHERE 문화재순번 = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, 문화재순번1);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        imageData = resultSet.getBytes("이미지2");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return imageData;
    }

    // 텍스트 영역을 생성하는 메서드
    private JTextArea createTextArea(String text1, int x, int y, int width, int height) {
        JTextArea area = new JTextArea();
        area.setBounds(x, y, width, height);
        area.setText(text1);
        return area;
    }
    
    private String getDataimage(String 문화재순번1ToFind) throws Exception {
        try (Connection db = DriverManager.getConnection(url, id, pw)) {
            String sql = "SELECT 이미지2 FROM culture_heritage WHERE 문화재순번 = ?";
            try (PreparedStatement pstmt = db.prepareStatement(sql)) {
                pstmt.setString(1, 문화재순번1ToFind);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("이미지2");
                    }
                }
            }
        }
        return null;
    }
    
    private String getDatanumber(String 문화재순번ToFind) throws Exception {
        try (Connection db = DriverManager.getConnection(url, id, pw)) {
            String sql = "SELECT 문화재순번 FROM culture_heritage WHERE 문화재순번 = ?";
            try (PreparedStatement pstmt = db.prepareStatement(sql)) {
                pstmt.setString(1, 문화재순번ToFind);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("문화재순번");
                    }
                }
            }
        }
        return null;
    }
    
    private String getDataname(String 문화재순번1ToFind) throws Exception {
        try (Connection db = DriverManager.getConnection(url, id, pw)) {
            String sql = "SELECT 문화재명 FROM culture_heritage WHERE 문화재순번 = ?";
            try (PreparedStatement pstmt = db.prepareStatement(sql)) {
                pstmt.setString(1, 문화재순번1ToFind);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("문화재명");
                    }
                }
            }
        }
        return null;
    }
    
    private JLabel createImageLabel(String imagePath, int x, int y, int width, int height) {
        JLabel label = new JLabel();
        label.setBounds(x, y, width, height);

        if (imagePath != null) {
            ImageIcon imageIcon = createImageIconFromDatabase(imagePath);
            label.setIcon(imageIcon);
        }

        return label;
    }
    
    private BufferedImage getImageFromDatabase(String imageName) throws Exception {
        try (Connection db = DriverManager.getConnection(url, id, pw)) {
            String sql = "SELECT 이미지2 FROM culture_heritage WHERE 문화재순번 = ?";
            try (PreparedStatement pstmt = db.prepareStatement(sql)) {
                pstmt.setString(1, imageName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // BLOB 컬럼에서 이미지 데이터를 가져와 ByteArrayInputStream으로 변환
                        byte[] imageData = rs.getBytes("이미지2");
                        if (imageData != null&& imageData.length > 0) {
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
                            // ByteArrayInputStream으로부터 BufferedImage 생성
                            return ImageIO.read(inputStream);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private ImageIcon createImageIconFromDatabase(String imageName) {
        try (Connection db = DriverManager.getConnection(url, id, pw)) {
            String sql = "SELECT 이미지2 FROM culture_heritage WHERE 문화재순번 = ?";
            try (PreparedStatement pstmt = db.prepareStatement(sql)) {
                pstmt.setString(1, imageName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // BLOB 컬럼에서 이미지 데이터를 가져오기 위해 getBinaryStream 사용
                        InputStream inputStream = rs.getBinaryStream("이미지2");
                        if (inputStream != null) {
                            // InputStream에서 바이트 배열로 변환
                            byte[] imageBytes = inputStream.readAllBytes();
                            // 바이트 배열에서 ImageIcon 생성
                            return new ImageIcon(imageBytes);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    private static class JLabelWithBackground extends JLabel {
        private BufferedImage image;

        public JLabelWithBackground(BufferedImage image) {
            this.image = image;
        }

        public JLabelWithBackground(String imagePath) {
            // 이미지를 경로에서 로드하고 배경으로 설정
            this.image = loadImageFromPath(imagePath);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
        
        private BufferedImage loadImageFromPath(String imagePath) {
            try {
                return ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }







    // 데이터베이스에서 전화번호 정보를 가져오는 메서드
    private String getDatadate(String 문화재순번ToFind) throws Exception {
        try (Connection db = DriverManager.getConnection(url, id, pw)) {
            String sql = "SELECT 재정일자 FROM culture_heritage WHERE 문화재순번 = ?";
            try (PreparedStatement pstmt = db.prepareStatement(sql)) {
                pstmt.setString(1, 문화재순번ToFind);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("재정일자");
                    }
                }
            }
        }
        return null;
    }
    
    private String getDataaddress(String 문화재순번ToFind) throws Exception {
        try (Connection db = DriverManager.getConnection(url, id, pw)) {
            String sql = "SELECT 주소 FROM culture_heritage WHERE 문화재순번 = ?";
            try (PreparedStatement pstmt = db.prepareStatement(sql)) {
                pstmt.setString(1, 문화재순번ToFind);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("주소");
                    }
                }
            }
        }
        return null;
    }
    
    
    private JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        return label;
    }
    // 버튼을 생성하는 메서드
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBorderPainted(true);
        button.setContentAreaFilled(false);
        button.setFocusPainted(true);
        return button;
    }

    // 이미지 버튼을 생성하는 메서드
    private JButton imageButton(String imagePath, int x, int y, int width, int height) {
        JButton button = new JButton(new ImageIcon(imagePath));
        button.setBounds(x, y, width, height);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }

    // 이미지 선택을 처리하는 메서드
    

    


    // 파일을 복사하는 메서드
    
    
}

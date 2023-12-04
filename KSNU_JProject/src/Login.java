import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class Login extends JPanel {
	static final String driver = "oracle.jdbc.OracleDriver";
    static final String url = "jdbc:oracle:thin:@localhost:1521:xe";
    static final String id = "C##jaehun";
    static final String pw = "1234";
    private JButton login;
    private JButton signUp;
    private JTextField useridField;
    private JPasswordField passwordField;
    private JTextArea text;
    private JTextArea textOr;
    private JButton title;
    private String username;
    Color siennaColor = new Color(255, 53, 051);
    public Login() {
    	
        setBounds(750, 200, 400, 410);
        setBackground(Color.WHITE);
        setLayout(null); // 절대 위치 지정
        
        useridField = createTextField("사용할 ID를 입력하시오.",200-150, 210-100+10, 300, 50);
        passwordField = createPasswordField("                    ",200-150, 260-100+10, 300, 50);
        // 텍스트 상자
        useridField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (useridField.getText().equals("사용할 ID를 입력하시오.")) {
                    useridField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (useridField.getText().isEmpty()) {
                    useridField.setText("사용할 ID를 입력하시오.");
                }
            }
        });
        // 비밀번호
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passwordField.getText().equals("                    ")) {
                	passwordField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getText().isEmpty()) {
                	passwordField.setText("                    ");
                } 
            }
        });
        
        //버튼
        login = createButton("로그인", 200-150, 330-100+10, 300, 40);
        login.setBackground(siennaColor);
        login.setContentAreaFilled(true);
        text = createTextArea("계정이 없으신가요",250-150, 425-100+10, 100, 50);
        signUp = createButton("회원가입", 350-140, 420-100+10, 100, 30);
        title = createimgButton("images/logo.png", 200-115, 25+10, 230, 70);
        textOr = createTextArea("  또는",330-150, 390-100+10, 40, 30);
        

        this.add(useridField);
        this.add(passwordField);
        this.add(textOr);
        this.add(text);
        this.add(login);
        this.add(signUp);
        this.add(title);
        
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userid = useridField.getText();
                String password = new String(passwordField.getPassword());
                
                try {
                    if (checkCredentials(userid, password)) {
                    	String username = getUsernameFromDatabase(userid);
                        JOptionPane.showMessageDialog(null,username + "님 로그인 성공!");
                        //Profile profile = new Profile(username);
                        // 여기에서 로그인 성공 후의 동작 추가
                        Menu menu = new Menu(userid);
                        
                          // currentPanel에 scrollPane 할당
                        
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(Login.this);
                        menu.setVisible(true);
                       
                        frame.dispose();
                        frame.add(menu);
                        frame.revalidate();
                        frame.repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "로그인 실패. 이름 또는 비밀번호를 확인하세요.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
       
        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sign_up sign_up = new Sign_up();
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(Login.this);
                sign_up.setVisible(true);
                //frame.dispose();
                
                frame.dispose();
                frame.add(sign_up);
                frame.revalidate();
                frame.repaint();
                
                
                //sign_up.setVisible(true);
                //frame.dispose();
                //frame.setContentPane(sign_up); // Set Sign_up as the content pane
                //frame.revalidate();
                //frame.repaint();
            }
        }); 
        
        
        
    }
    
    private String getUsernameFromDatabase(String userId) throws Exception {
        Class.forName(driver);
        Connection db = DriverManager.getConnection(url, id, pw);

        String sql = "SELECT username FROM user_info WHERE userid = ?";
        PreparedStatement pstmt = db.prepareStatement(sql);
        pstmt.setString(1, userId);

        ResultSet rs = pstmt.executeQuery();

        String username = "";
        if (rs.next()) {
            username = rs.getString("username");
        }

        db.close();

        return username;
    }

    
    
    private JTextField createTextField(String text, int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        //textField.setBorder(new RoundedBorder(Color.BLACK, 10, 10));
        textField.setText(text);
        return textField;
    }

    private JPasswordField createPasswordField(String text, int x, int y, int width, int height) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(x, y, width, height);
        passwordField.setText(text);
        return passwordField;
    }

    private JTextArea createTextArea(String text1,int x, int y, int width, int height) {
        JTextArea area = new JTextArea();
        area.setBounds(x, y, width, height);
        area.setText(text1);
        return area;
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBorderPainted(true);
        button.setContentAreaFilled(false);
        button.setFocusPainted(true);
      
        return button;
    }
    private JButton createimgButton(String imagePath, int x, int y, int width, int height) {
        JButton button = new JButton(new ImageIcon(imagePath));
        button.setBounds(x, y, width, height);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }

    
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        int topX = 0;
        int topY =0;
        int bottomX = 399;
        int bottomY = 409;

        g.drawLine(topX+50, bottomY-100, bottomX-50, bottomY-100);
        g.drawLine(topX, topY, topX, bottomY); // 왼쪽 세로선
        g.drawLine(topX, topY, bottomX, topY); // 위 가로선
        
        g.drawLine(bottomX, topY, bottomX, bottomY); // 오른쪽 세로선
        g.drawLine(topX, bottomY, bottomX, bottomY); // 아래 가로선
    }
    public static boolean checkCredentials(String usernameToFind, String passwordToFind) throws Exception {
        Class.forName(driver);
        Connection db = DriverManager.getConnection(url, id, pw);

        String sql = "SELECT * FROM user_info WHERE userid = ? AND password = ?";
        PreparedStatement pstmt = db.prepareStatement(sql);
        pstmt.setString(1, usernameToFind);
        pstmt.setString(2, passwordToFind);
        
        ResultSet rs = pstmt.executeQuery();
        
        boolean credentialsMatch = rs.next();
        
        db.close();

        return credentialsMatch;
    }

}
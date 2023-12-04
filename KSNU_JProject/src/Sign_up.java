import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Sign_up extends JFrame {

    static final String driver = "oracle.jdbc.OracleDriver";
    static final String url = "jdbc:oracle:thin:@localhost:1521:xe";
    static final String id = "C##JAEHUN";
    static final String pw = "1234";
    private JTextField usernameField, userPhonenumberField, userPasswordField, userIdField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    private JButton login;
    private JButton home;
    private JTextArea text, textOr;
    private static String username;

    Color siennaColor = new Color(255, 100, 51);
    int topx=0;
    int topy=0;


    public Sign_up() {
    	 setBounds(800,100,500,700);
         setLayout(null);
         getContentPane().setBackground(Color.white);
         // 기타 창 설정 (닫기 버튼 동작, 가시성 설정 등)
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setResizable(false);
         setVisible(false);




        

        home = createimgButton("images/logo.png", topx+130, topy+120, 230, 40);
        text = createTextArea(" 공주시의 문화재를 보려면 가입하세요.", topx+125, topy+185, 300, 20);
        login = createButton("로그인 화면으로 돌아가기 ",  topx+95, topy+220, 300, 40);
        login.setBackground(siennaColor);
        login.setContentAreaFilled(true);
        textOr = createTextArea("또는", topx+240, topy+280,40, 30);

        userIdField = createTextField("  사용할 ID를 입력하시오.", topx+95, topy+320, 300, 40);
        usernameField = createTextField("  성명", topx + 95, topy+370, 300, 40);
        userPhonenumberField = createTextField(" 사용자 전화번호", topx+95,topy+420, 300, 40);
        passwordField = createPasswordField("      ", topx + 95, topy+470, 300, 40);
        signUpButton = createButton("가입", topx + 95, topy+520, 300, 40);
        signUpButton.setBackground(siennaColor);
        signUpButton.setContentAreaFilled(true);

        text.setFont(new Font("맑은고딕", Font.ITALIC, 13)); // 폰트 크기 변경
        text.setForeground(Color.GRAY); // 텍스트 색상 변경
        add(userIdField);
        add(usernameField);
        add(userIdField);
        add(userPhonenumberField);
        add(passwordField);

        add(textOr);
        add(signUpButton);
        add(home);
        add(text);
        add(login);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main login = new Main();
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(Sign_up.this);
                login.setVisible(true);
                frame.dispose();
                frame.setContentPane(login);
                //frame.revalidate();
                frame.repaint();
               
                
                
               
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userid = userIdField.getText();
                String username = usernameField.getText();
                String phonenumber = userPhonenumberField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    addData(userid, username, phonenumber, password);
                    JOptionPane.showMessageDialog(null, "가입이 완료되었습니다!");
                    Menu menu = new Menu(username);
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(Sign_up.this);
                    menu.setVisible(true);
                    //frame.dispose();
                    frame.setContentPane(menu);
                    frame.revalidate();
                    frame.repaint();

                } catch (Exception ex) {
                    ex.printStackTrace();

                }
            }
        });

        userIdField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (userIdField.getText().equals("  사용할 ID를 입력하시오.")) {
                    userIdField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (userIdField.getText().isEmpty()) {
                    userIdField.setText("  사용할 ID를 입력하시오.");
                }
            }
        });

        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("  성명")) {
                    usernameField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("  성명");
                }
            }
        });

        userPhonenumberField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (userPhonenumberField.getText().equals(" 사용자 전화번호")) {
                    userPhonenumberField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (userPhonenumberField.getText().isEmpty()) {
                    userPhonenumberField.setText(" 사용자 전화번호");
                }
            }
        });

        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passwordField.getText().equals("      ")) {
                    passwordField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getText().isEmpty()) {
                    passwordField.setText("      ");
                }
            }
        });
    }

    private JTextField createTextField(String text, int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        textField.setText(text);
        return textField;
    }

    private JTextArea createTextArea(String text1, int x, int y, int width, int height) {
        JTextArea area = new JTextArea();
        area.setBounds(x, y, width, height);
        area.setText(text1);
        return area;
    }

    private JPasswordField createPasswordField(String text, int x, int y, int width, int height) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(x, y, width, height);
        passwordField.setText(text);
        return passwordField;
    }

    private JButton createimgButton(String imagePath, int x, int y, int width, int height) {
        JButton button = new JButton(new ImageIcon(imagePath));
        button.setBounds(x, y, width, height);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBorderPainted(true);
        button.setContentAreaFilled(false);
        button.setFocusPainted(true);

        return button;
    }

    private void signUp() {
        String userid = userIdField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String phonenumber = userPhonenumberField.getText();

        // 여기에서 회원가입 처리를 수행하거나, 필요한 경우 데이터를 출력합니다.
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("회원가입");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Sign_up signUpPanel = new Sign_up();
        frame.setContentPane(signUpPanel);

        frame.pack();
        frame.setSize(500, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.LIGHT_GRAY);
        int topX = 50;
        int topY =50;
        int bottomX = 450;
        int bottomY = 650;

        
        g.drawLine(topX, topY, topX, bottomY); // 왼쪽 세로선
        g.drawLine(topX, topY, bottomX, topY); // 위 가로선
        
        g.drawLine(bottomX, topY, bottomX, bottomY); // 오른쪽 세로선
        g.drawLine(topX, bottomY, bottomX, bottomY); // 아래 가로선
    }

    public static void addData(String userid, String username, String userPhonenumber, String password) throws Exception {
        Class.forName(driver);
        Connection db = DriverManager.getConnection(url, id, pw);

        String sql = "INSERT INTO user_info (userid, username, userPhonenumber, password) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = db.prepareStatement(sql);
        pstmt.setString(1, userid);
        pstmt.setString(2, username);
        pstmt.setString(3, userPhonenumber);
        pstmt.setString(4, password);
        pstmt.execute();

        db.close();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class ItemData {
    private byte[] imageData;
    private String 문화재순번;

    public ItemData(byte[] imageData, String 문화재순번) {
        this.imageData = imageData;
        this.문화재순번 = 문화재순번;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String get문화재순번() {
        return 문화재순번;
    }
}

public class Menu extends JFrame {
    private JButton home;
    //private JButton location;
    private JButton profile;
    private JButton logout;
    private JButton[] itemButtons;
    private Profile profilePanel;
    private JScrollPane scrollPane;  
    private final int SMALL_WIDTH = 120;
    private final int SMALL_HEIGHT = 120;
    private String username;
    private JComponent currentPanel;
    

    public Menu(String username) {
        this.setResizable(false);
        this.setVisible(true);
        this.setPreferredSize(new Dimension(800, 600 * 8 / 5));
        this.setSize(800, 600 * 8 / 5);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        // 패널의 크기를 설정합니다.
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(800, 600 * 8 / 5));
        panel.setBackground(Color.WHITE);
        
        JButton logo = createButton("images/메인 사진2.png","메인 사진2", 0, 10, 800, 120);
        JButton logoB = createButton("images/LogoB_button1.png", "LogoB",13, 150, 43, 45);
        home = createButton("images/home_button1.png", "home", 13, 270, 43, 30);
        //location = createButton("images/location_button1.png", "location", 13, 340, 43, 40);
        profile = createButton("images/profile_button1.png", "profile", 13, 740, 43, 50);
        logout = createButton("images/logout_button1.png", "logout", 10, 830, 50, 50);

        
        
        // 기존 패널에 버튼들 추가
        panel.add(logo);
        panel.add(logoB);
        panel.add(home);
        //panel.add(location);
        //panel.add(profile);
        panel.add(logout);
        
        JPanel scrollablePanel = new JPanel();
        scrollablePanel.setLayout(null);
        scrollablePanel.setBackground(Color.white);

        // 데이터베이스에서 전체 아이템 정보 가져오기
        List<ItemData> itemList = getItemListFromDatabase();
        int totalItemCount = itemList.size();
        itemButtons = new JButton[totalItemCount];
        
        

        int startX = 100;
        int startY = 70;
        int buttonSpacing = 70;

        int buttonIndex = 1;

        for (int i = 0; i < totalItemCount; i++) {
            ItemData itemData = itemList.get(i);

            // 이미지 데이터를 ImageIcon으로 변환
            ImageIcon imageIcon = new ImageIcon(itemData.getImageData());

            itemButtons[i] = createButton(imageIcon, "item" + i,
                    startX + (i % 3) * (SMALL_WIDTH + buttonSpacing),
                    startY + (i / 3) * (SMALL_HEIGHT + buttonSpacing),
                    SMALL_WIDTH, SMALL_HEIGHT);

            itemButtons[i].setText(String.valueOf(i + 1));
            scrollablePanel.add(itemButtons[i]);

            int finalI = i;
            // 각 버튼에 ActionListener 등록
            itemButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    
                    // 기존 패널을 제거하고
                    if (currentPanel != null) {
                        panel.remove(currentPanel);
                    }
                    
                    // 해당 아이템의 문화재순번을 가져옴
                    String 문화재순번1 = itemList.get(finalI).get문화재순번();
                    
                    panel.repaint();   
                    // Profile 패널을 생성하고
                    profilePanel = new Profile(문화재순번1);
                    profilePanel.setBounds(0, 0, 800, 600 * 8 / 5);  // Profile 패널의 크기와 위치를 설정
                                     // 패널에 추가합니다
                    panel.add(profilePanel);
                    currentPanel = profilePanel;
                    panel.repaint();
                    // 패널을 다시 그립니다
                    
                    
                }
            });
        }

        
        

        int rows = totalItemCount / 3; // 한 행에 3개의 아이템이 들어간다고 가정
        if (totalItemCount % 3 > 0) rows++; // 아이템의 갯수가 3의 배수가 아니라면 행을 하나 더 추가

        int scrollablePanelHeight = startY + rows * (SMALL_HEIGHT + buttonSpacing); // 패널의 세로 길이 계산

        scrollablePanel.setPreferredSize(new Dimension(800, scrollablePanelHeight)); // 패널의 크기 조정

        JScrollPane scrollPane = new JScrollPane(scrollablePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
     // 기존의 JScrollPane scrollPane = new JScrollPane(scrollablePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 라인을 제거해주세요.
        this.scrollPane = new JScrollPane(scrollablePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setBounds(76, 130, 725, logout.getY() - (logoB.getY() + logoB.getHeight())+200);
                
        // scrollPanel에 scrollPane 추가
        panel.add(this.scrollPane);
                
        this.add(panel);
        

        // ActionListener 설정
        //profile.addActionListener(createItemActionListener(profile, panel, new Profile(username)));
        //location.addActionListener(createItemActionListener(location, panel, new Location()));
        
        
        
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPanel != null) {
                    panel.remove(currentPanel);
                }
                panel.add(Menu.this.scrollPane);  // scrollPane을 다시 추가
                currentPanel = Menu.this.scrollPane;  // currentPanel에 scrollPane 할당
                panel.repaint();
            }
        });
        
       
        // 마우스 이벤트 처리
        handleMouseEvents();
    }

    
    
    private void handleMouseEvents() {
        MouseAdapter mouseAdapter = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                JButton button = (JButton) evt.getSource();
                String buttonName = button.getName();
                button.setIcon(new ImageIcon("images/" + buttonName + "_button2.png"));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                JButton button = (JButton) evt.getSource();
                String buttonName = button.getName();
                button.setIcon(new ImageIcon("images/" + buttonName + "_button1.png"));
            }
        };

        // 각 버튼에 마우스 이벤트 리스너 등록
        home.addMouseListener(mouseAdapter);
        //location.addMouseListener(mouseAdapter);
        profile.addMouseListener(mouseAdapter);
        logout.addMouseListener(mouseAdapter);
        
        logout.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                username = null;
                dispose();
                Main login = new Main();
                JFrame loginFrame = new JFrame();  // 새로운 JFrame 객체 생성
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setPreferredSize(new Dimension(800, (600 * 8 / 5) - 81));  // 창 크기 설정
                loginFrame.add(login);  // Login 객체를 JFrame에 추가
                loginFrame.pack();  // 창 크기를 내용에 맞게 조절
                loginFrame.setLocationRelativeTo(null);  // 창을 화면 중앙에 위치시킴
                loginFrame.setVisible(true);  // 창을 보이게 함
            }
            
          
        });
        
        home.doClick();
        
        
        
        

    }
    
    
    public void paintComponent(Graphics g) {
        super.paint(g);
        g.setColor(Color.LIGHT_GRAY);
       

        g.drawLine(75, 0, 1, 600*8/5);
       
    }

    private List<ItemData> getItemListFromDatabase() {
        List<ItemData> itemList = new ArrayList<>();

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "C##jaehun", "1234")) {
            String query = "SELECT 이미지, 문화재순번 FROM CULTURE_HERITAGE";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    byte[] imageData = resultSet.getBytes("이미지");
                    String 문화재순번 = resultSet.getString("문화재순번");
                    itemList.add(new ItemData(imageData, 문화재순번));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemList;
    }

    private ActionListener createItemActionListener(JButton button, JPanel panel, JPanel contentPanel) {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPanel != null) {
                    panel.remove(currentPanel);
                }

                contentPanel.setBounds(0, 0, 800, 600 * 8 / 5);
                panel.add(contentPanel);
                currentPanel = contentPanel;
                panel.repaint();
            }
        };
    }
    
    
    

    private JButton createButton(Object image, String name, int x, int y, int width, int height) {
        JButton button;

        if (image instanceof String) {
            // If the image is a path, create a button with ImageIcon
            button = new JButton(new ImageIcon((String) image));
        } else if (image instanceof ImageIcon) {
            // If the image is already an ImageIcon, use it directly
            button = new JButton((ImageIcon) image);
        } else {
            // Handle other cases or throw an exception as needed
            throw new IllegalArgumentException("Invalid image type");
        }

        button.setBounds(x, y, width, height);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setName(name);  // 버튼의 이름을 설정

        return button;
    }
}

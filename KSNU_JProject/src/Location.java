import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;

public class Location extends JPanel {
    private GoogleAPI googleAPI = new GoogleAPI();
    private String location = "전라북도 군산시 진포로 73";
    private JLabel googleMap;

    Location() {
        setLayout(null); // Layout Manager를 사용하지 않음
        setBackground(Color.WHITE);

        // JComboBox를 생성
        String[] comboBoxItems = {"군산24시제일동물병원", "도담동물병원", "쿨펫동물병원", "수송반려동물병원", "더사랑동물병원",
        		"오션동물병원", "가족동물병원", "해동가축병원", "스마일동물병원", "해맑은 동물병원", "푸른동물병원",
        		"연합가축병원", "금강동물병원", "스마일동물병원", "미소동물병원", "누리동물병원", "금강종합동물병원", "군옥동물병원", "서해동물병원"};
        JComboBox<String> comboBox = new JComboBox<>(comboBoxItems);

        // JComboBox의 선택 이벤트 처리
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMenu = (String) comboBox.getSelectedItem();

                if (selectedMenu.equals("군산24시제일동물병원")) {
                    location = "전라북도 군산시 진포로 73";
                }
                else if(selectedMenu.equals("도담동물병원")) {
                	location = "전라북도 군산시 수송로 291 2층";
                }
                else if(selectedMenu.equals("쿨펫동물병원")) {
                	location = "전라북도 군산시 구암3.1로 137 이마트 2층";
                }
                else if(selectedMenu.equals("수송반려동물병원")) {
                	location = "전라북도 군산시 수송로 208 수송반려동물병원";
                }
                else if(selectedMenu.equals("더사랑동물병원")) {
                	location = "전라북도 군산시 문화로 121";
                }
                else if(selectedMenu.equals("오션동물병원")) {
                	location = "전라북도 군산시 백릉로 151 더테라스 205호";
                }
                else if(selectedMenu.equals("가족동물병원")) {
                	location = "전라북도 군산시 월명로 211 수송동 LG서비스센터 맞은편 가족동물병원";
                }
                else if(selectedMenu.equals("해동가축병원")) {
                	location = "전라북도 군산시 중앙로 59 해동가축병원약품";
                }
                else if(selectedMenu.equals("스마일동물병원")) {
                	location = "전라북도 군산시 공단대로 429 스마일동물병원";
                }
                else if(selectedMenu.equals("해맑은 동물병원")) {
                	location = "전라북도 군산시 하나운로 75";
                }
                else if(selectedMenu.equals("푸른동물병원")) {
                	location = "전라북도 군산시 팔마로 154-1";
                }
                else if(selectedMenu.equals("연합가축병원")) {
                	location = "전라북도 군산시 중앙로 61";
                }
                else if(selectedMenu.equals("금강동물병원")) {
                	location = "전라북도 군산시 미원로 132-3";
                }
                else if(selectedMenu.equals("스마일동물병원")) {
                	location = "전라북도 군산시 대학로 355";
                }
                else if(selectedMenu.equals("미소동물병원")) {
                	location = "전라북도 군산시 대야면 만자로 28";
                }
                else if(selectedMenu.equals("누리동물병원")) {
                	location = "전라북도 군산시 월명로 509-3 삼화서점";
                }
                else if(selectedMenu.equals("금강종합동물병원")) {
                	location = "전라북도 군산시 미원로 132-3";
                }
                else if(selectedMenu.equals("군옥동물병원")) {
                	location = "전라북도 군산시 대야면 대야시장로 11";
                }
                else if(selectedMenu.equals("서해동물병원")) {
                	location = "전라북도 군산시 대야면 대야관통로 72";
                }
                
                googleAPI.downloadMap(location);

                // 이미지 크기를 동적으로 가져오기
                ImageIcon mapIcon = googleAPI.getMap(location, 500, 500);  // 원하는 크기로 조절
                googleMap = new JLabel(mapIcon);
                googleAPI.fileDelete(location);

                // 이미지 크기에 맞게 레이블의 크기를 설정
                googleMap.setBounds(-20, 0, 725, 880);
                add(googleMap);
                setVisible(true);
            }
        });

        // 콤보박스의 위치와 크기를 설정
        comboBox.setBounds(240, 30, 200, 30);

        // 콤보박스를 패널에 추가
        add(comboBox);
    }
}

class GoogleAPI {
    public void downloadMap(String location) {
        try {
            String imageURL = "https://maps.googleapis.com/maps/api/staticmap?center=Z%C3%BCrich"
                    + URLEncoder.encode(location, "UTF-8") + "&zoom=15&size=800x800&key=AIzaSyC5wappkW1MWC23anb6Fi7tWl8gAx4OE44";
            URL url = new URL(imageURL);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(location);
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageIcon getMap(String location, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(location);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public void fileDelete(String fileName) {
        File f = new File(fileName);
        f.delete();
    }
}

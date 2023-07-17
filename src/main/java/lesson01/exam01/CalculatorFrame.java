package lesson01.exam01;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CalculatorFrame extends JFrame implements ActionListener {
	// 데스크톱 애플리케이션 실습 - 계산기
	// 자바에서 기본으로 제공하는 Swing GUI 컴포넌트 이용해 UI 생성
	// CalculatorFrame 클래스 윈도우 기능을 갖기 위해 JFrame 클래스 상속 받는다.
	// "=" 버튼과 "Clear" 버튼의 클릭 이벤트를 처리하는 역할(리스너)도 수행하기 위해 ActionListener 인터페이스 구현한다.

	JTextField operand1 = new JTextField(4);
	JTextField operand2 = new JTextField(4);
	String[] operatorData = { "+", "-", "*", "/" };
	JComboBox<String> operator = new JComboBox<String>(operatorData);
	JButton equal = new JButton("=");
	JTextField result = new JTextField(6);
	JButton clear = new JButton("Clear");

	public CalculatorFrame() {
		this.setTitle("Lesson01-Exam01");

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		contentPane.add(Box.createVerticalGlue());
		contentPane.add(this.createInputForm());
		contentPane.add(this.createToolBar());
		contentPane.add(Box.createVerticalGlue());

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent event) { // "=" 버튼과 "Clear" 버튼의 클릭하게 되면 actionPerformed() 호출된다.
		// 먼저 어떤 버튼이 클릭 됐는지 구분하기 위해 event.getSource() 반환값 검사
		if (event.getSource() == equal) { // "=" 버튼인 경우
			compute(); // 계산 수행
		} else { // "Clear" 버튼인 경우
			clearForm(); // 입력상자 초기화
		}
	}

	private void compute() { // 계산 수행
		double a = Double.parseDouble(operand1.getText());
		double b = Double.parseDouble(operand2.getText());
		double r = 0;

		try {
			switch (operator.getSelectedItem().toString()) {
			case "+":
				r = a + b;
				break;
			case "-":
				r = a - b;
				break;
			case "*":
				r = a * b;
				break;
			case "/":
				if (b == 0)
					throw new Exception("0 으로 나눌 수 없습니다!");
				r = a / b;
				break;
			}

			result.setText(Double.toString(r));

		} catch (Exception err) {
			JOptionPane.showMessageDialog(null, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void clearForm() { // 입력상자 초기화
		this.operand1.setText("");
		this.operand2.setText("");
		this.result.setText("");
	}

	private Box createInputForm() {
		Box box = Box.createHorizontalBox();
		box.setMaximumSize(new Dimension(300, 30));
		box.setAlignmentY(Box.CENTER_ALIGNMENT);
		box.add(operand1);
		box.add(operator);
		box.add(operand2);
		box.add(equal);
		box.add(result);
		equal.addActionListener(this);
		return box;
	}

	private Box createToolBar() {
		Box box = Box.createHorizontalBox();
		box.add(clear);
		clear.addActionListener(this);
		return box;
	}

	public static void main(String[] args) {
		CalculatorFrame app = new CalculatorFrame();
		app.setVisible(true);
	}

	// 데스크톱 애플리케이션의 문제점
	// 데스크톱 애플리케이션은 PC에 설치되어 실행하기 때문에 웹 애플리케이션보다는 실행 속도가 빠르다.
	// 하지만 다음 문제 때문에 기업용 애플리케이션의 아키텍처로 쓰기에는 적합하지 않다.
	// 기능을 추가하거나 변경할때마다 다시 배포해야하므로 매우 번거롭다.
	// 데이터베이스에 접속이 필요한 애플리케이션은 보안에 특히 취약하다.
	// 해결 방안
	// 번거로운 배포 문제는 자동 갱신을 통해 해결할 수 있다.
	// 즉 애플리케이션을 실행할 때 먼저 서버에 갱신된 버전이 있는지 조회하고, 만약 있다면 내려받기해 재설치 한후 실행하면 된다.
	// 하지만, 보안이 취약한 문제는 여전히 남아있다...
}

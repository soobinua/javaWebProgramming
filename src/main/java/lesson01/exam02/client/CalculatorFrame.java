package lesson01.exam02.client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CalculatorFrame extends JFrame implements ActionListener { // 계산기 클라이언트
	CalculatorAgent calcAgent;
	JTextField operand1 = new JTextField(4);
	JTextField operator = new JTextField(2);
	JTextField operand2 = new JTextField(4);
	JButton equal = new JButton("=");
	JTextField result = new JTextField(6);
	JButton clear = new JButton("Clear");

	public CalculatorFrame() {
		try {
			calcAgent = new CalculatorAgent("localhost", 8888);
		} catch (Exception err) {
			JOptionPane.showMessageDialog(null, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		this.setTitle("Lesson01-Exam02");

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		contentPane.add(Box.createVerticalGlue());
		contentPane.add(this.createInputForm());
		contentPane.add(this.createToolBar());
		contentPane.add(Box.createVerticalGlue());

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				calcAgent.close();
				System.exit(0);
			}
		});

		this.pack();
		this.setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent event) { // 값과 연산자 입력 후 "=" 버튼 클릭하면 actionPerformed() 호출
		if (event.getSource() == equal) { // "=" 버튼인 경우 compute() 호출
			compute();
		} else {
			clearForm();
		}
	}

	private void compute() { // CalculatorAgent 사용해 연산 수행
		double a = Double.parseDouble(operand1.getText());
		double b = Double.parseDouble(operand2.getText());
		double r = 0;

		try { // 예외처리 try ~ catch
				// 만약 0으로 나눈다거나 서버에서 지원하지 않는 연산자를 입력한 경우 서버로부터 연산이 실패했음을 알리는 결과를 받는다.
				// CalculatorAgent의 compute()는 예외를 던진다.
			r = calcAgent.compute(operator.getText(), a, b);
			result.setText(Double.toString(r));

		} catch (Exception err) {
			JOptionPane.showMessageDialog(null, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void clearForm() {
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

	// Client
	// 프레젠테이션 로직
	// Calculator Frame -사용-> Calculator Agent <-네트워크 통신-> Calculator Server
}

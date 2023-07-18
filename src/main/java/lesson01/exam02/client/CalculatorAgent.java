package lesson01.exam02.client;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class CalculatorAgent { // 서버 측과의 통신을 담당하는 객체
	Socket socket = null;
	PrintStream out = null;
	Scanner in = null;

	public CalculatorAgent(String ip, int port) throws Exception {
		// CalculatorAgent 생성자는 소켓을 통해 입출력할 수 있도록 스트림 객체 준비
		socket = new Socket(ip, port);
		out = new PrintStream(socket.getOutputStream());
		in = new Scanner(socket.getInputStream());
	}

	public double compute(String operator, double a, double b) throws Exception { // 사용자가 입력한 연산자와 두 개의 입력값을 서버에 전달
		try {
			out.println(operator);
			out.println(a);
			out.println(b);
			out.flush();

			String state = in.nextLine(); // 서버로부터 계산 결과를 받으면 먼저 상태 조사
			if (state.equals("success")) { // 상태가 "success"라면 정상적으로 처리되었다는 것으로
				return Double.parseDouble(in.nextLine()); // 다음 라인을 읽어서 서버가 보낸 값을 double 형으로 변환해 리턴
			} else { // 그 밖에는 오류가 발생했다는 것이므로
				throw new Exception(in.nextLine()); // 다음 라인을 읽어 오류 메시지를 포함한 예외를 던진다.
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void close() {
		try {
			out.println("goodbye");
			System.out.println(in.nextLine());
		} catch (Exception e) {
		}

		try {
			out.close();
		} catch (Exception e) {
		}
		try {
			in.close();
		} catch (Exception e) {
		}
		try {
			socket.close();
		} catch (Exception e) {
		}
	}
}

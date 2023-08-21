package lesson01.exam02.multiserver;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class CalculatorWorker extends Thread {
// CalculatorWorker가 스레드로서 기능을 수행하려면 Thread 상속 받아야한다.
	static int count;

	Socket socket;
	Scanner in;
	PrintStream out;
	int workerId;

	public CalculatorWorker(Socket socket) throws Exception {
		// 생성자에서는 클라이언트와 입출력을 하기 위한 스트림 객체 준비
		// 또한, 생성된 스레드를 구분하기 위해 클래스 변수 count를 증가시켜 스레드 고유 번호로 할당
		workerId = ++count;
		this.socket = socket;
		in = new Scanner(socket.getInputStream());
		out = new PrintStream(socket.getOutputStream());
	}

	// 스레드를 시작시키면 부모 스레드의 실행 흐름으로부터 분리되어 별개의 흐름으로 run()코드가 실행
	// 부모 스레드와 자식 스레드가 병행으로 작업을 수행
	@Override
	public void run() {
		// 기존 CalculatorServer의 processRequest()에 있던 코드
		System.out.println("[thread-" + workerId + "] processing the client request.");

		String operator = null;
		double a, b, r;

		while (true) {
			try {
				operator = in.nextLine();

				// 클라이언트에서 goodbye 보낼때까지 무한 반복하면서 계산 요청 처리
				if (operator.equals("goodbye")) {
					out.println("goodbye");
					break;

				} else {
					a = Double.parseDouble(in.nextLine());
					b = Double.parseDouble(in.nextLine());
					r = 0;

					switch (operator) {
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
					default:
						throw new Exception("해당 연산을 지원하지 않습니다!");
					}
					out.println("success");
					out.println(r);
				}

			} catch (Exception err) {
				out.println("failure");
				out.println(err.getMessage());
			}
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

		System.out.println("[thread-" + workerId + "] closed client.");
	}
}
//문제점
//C/S 클라이언트/서버 환경에서의 프로그래밍은 데스크톱 애플리케이션보다 더 복잡
//데이터 통신을 위한 네트워크 프로그래밍도 해야 하고
//다중 클라이언트의 요청을 처리하기 위해 스레드 프로그래밍도 해야한다.
//이외에도 DB연결 관리하거나 트랜잭션, 보안, 자바 빈 등 다양한 애플리케이션 자원을 관리하기 위한 프로그래밍도 필요

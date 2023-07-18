package lesson01.exam02.server;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class CalculatorServer { // 계산기 서버
	private int port;

	public CalculatorServer(int port) {
		this.port = port;
	}

	@SuppressWarnings("resource")
	public void service() throws Exception {
		ServerSocket serverSocket = new ServerSocket(port); // main()에서 설정한 포트 번호로 서버 소켓 생성
		System.out.println("CalculatorServer startup:");

		Socket socket = null;

		while (true) {
			try {
				System.out.println("waiting client...");

				socket = serverSocket.accept(); // 클라이언트의 연결을 기다리다가 연결이 이루어지면 클라이언트의 요청을 처리 무한 반복
				// 동시 작업 불가 이유 - 클라이언트 연결이 들어오면 accept()는 연결을 승인하고 해당 클라리언트와의 대화를 위해 소켓을 리턴
				System.out.println("connected to client.");

				// 동시 작업 불가 이유 - 그리고 processRequest() 호출
				processRequest(socket); // 클라이언트의 요청 처리
				System.out.println("closed client.");

			} catch (Throwable e) {
				System.out.println("connection error!");
			}
		}
	}

	// 동시 작업 불가 이유 - processRequest() 메소드는 클라이언트가 연결을 끊을때까지 리턴하지 않게 되어 있다.
	// 따라서 이후에 들어오는 모든 클라이언트의 요청은 대기열에 등록되어 기다리게 된다.
	private void processRequest(Socket socket) throws Exception { // 클라이언트의 요청 처리
		Scanner in = new Scanner(socket.getInputStream());
		PrintStream out = new PrintStream(socket.getOutputStream()); // 클라이언트 소켓으로부터 입출력을 위한 스트림 객체 준비

		String operator = null;
		double a, b, r;

		while (true) { // 무한 반복하면서 클라이언트가 보낸 연산자와 값을 읽어 계산 수행하고 그 결과 응답
			try {
				operator = in.nextLine();

				if (operator.equals("goodbye")) { // 클라이언트로부터 "goodbye" 메시지 받으면 연결 종료
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
	}

	public static void main(String[] args) throws Exception {
		CalculatorServer app = new CalculatorServer(8888); // 통신 포트 번호를 8888로 설정하고 계산기 서비스를 실행
		app.service();
	}

	// Server
	// 비즈니스 로직
	// 데이터 로직
	// Calculator Server <-네트워크 통신-> Calculator Agent

	// 클라이언트•서버 애플리케이션
	// 업무 변화에 대응하기 쉽다.
	// 서버쪽에서 데이터베이스에 접속 -> 보안이 강화된다.
	// 이 예제 핵심. 서버에서 계산을 수행하고 그 결과를 클라이언트로 보내준다.
	// 신규 연산자가 추가되는 경우 서버쪽만 변경하면 된다.
	// 클라이언트는 바뀌지 않기 때문에 다시 설치할 필요가 없다.
	// 데스크톱 애플리케이션의 기능 일부를 서버에 이관하는 구조로 만들면 기능 변경이나 추가에 대해 보다 유연하게 대처할 수 있다.
	// 문제점. 한번에 하나의 클라이언트하고만 연결된다. 동시에 여러 클라이언트의 요청을 처리할 수 없다.
	// 현재 연결된 클라이언트와의 연결이 끊어질때까지 다른 클라이언트는 기다려야 한다.
	// CalculatorFrame 두 개 동시에 실행하는 경우 첫번째로 실행한 프로그램이 종료되어야 두 번째 프로그램 이용 가능
	// 동시 작업 불가 이유 - CalculatorServer는 클라이언트와 연결되면 해당 클라이언트가 연결을 끊을때까지 다른 클라이언트와 연결 요청을 승인하지 않는다.
	// 문제를 해결하기 위해 대부분의 서버 프로그램은 멀티 프로세스 또는 멀티 스레드와 같은 병행처리 방식을 이용한다.
}

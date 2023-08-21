package lesson01.exam02.multiserver;

import java.net.ServerSocket;
import java.net.Socket;

//스레드를 이용해 다중 클라이언트의 요청을 처리하는 계산기 서버
//다중 클라이언트 요청의 특징
//클라이언트의 요청 처리 부분을 별도의 작업으로 분리
//분리된 작업은 스레드에 정의
//다중 클라이언트의 요청이 동시에 병행 처리
public class CalculatorServer {
	private int port;

	public CalculatorServer(int port) {
		this.port = port;
	}

	public void service() throws Exception {
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("CalculatorServer startup:");

		Socket socket = null;

		while (true) {
			try {
				// 그리고 다시, 대기열에 있는 다른 클라이언트와의 연결 승인
				socket = serverSocket.accept();
				System.out.println("connected to client.");

				// 이전 절 CalculatorServer는 클라이언트와 연결이 이루어지면 직접 통신을 수행해 요청을 처리했으나
				// 개선된 코드에서는 클라이언트와 연결이 이루어지는 즉시 CalculatorWorker 스레드를 생성해 작업 위임
				new CalculatorWorker(socket).start();

			} catch (Throwable e) {
				System.out.println("connection error!");
			}
		}

	}

	public static void main(String[] args) throws Exception {
		CalculatorServer app = new CalculatorServer(8888);
		app.service();
	}
}

//문제점
//C/S 클라이언트/서버 환경에서의 프로그래밍은 데스크톱 애플리케이션보다 더 복잡
//데이터 통신을 위한 네트워크 프로그래밍도 해야 하고
//다중 클라이언트의 요청을 처리하기 위해 스레드 프로그래밍도 해야한다.
//이외에도 DB연결 관리하거나 트랜잭션, 보안, 자바 빈 등 다양한 애플리케이션 자원을 관리하기 위한 프로그래밍도 필요

package server;

import main.Const;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * ���ᯥ稢��� ࠡ��� �ணࠬ�� � ०��� �ࢥ�
 */
public class Server {

	
	/**
	 * ���樠�쭠� "����⪠" ��� ArrayList, ����� ���ᯥ稢��� ����� �
	 * ���ᨢ� �� ࠧ��� ��⥩
	 */
	private List<Connection> connections = 
			Collections.synchronizedList(new ArrayList<Connection>());
	private ServerSocket server;

	/**
	 * ��������� ᮧ���� �ࢥ�. ��⥬ ��� ������� ������祭�� ᮧ������
	 * ��ꥪ� Connection � �������� ��� � ᯨ᮪ ������祭��.
	 */
	public Server() {
		try {
			server = new ServerSocket(Const.Port);

			while (true) {
				Socket socket = server.accept();

				// ������ ��ꥪ� Connection � ������塞 ��� � ᯨ᮪
				Connection con = new Connection(socket);
				connections.add(con);

				// ���樠������� ���� � ����᪠�� ��⮤ run(),
				// ����� �믮������ �����६���� � ��⠫쭮� �ணࠬ���
				con.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeAll();
		}
	}

	/**
	 * ����뢠�� �� ��⮪� ��� ᮥ������� � ⠪�� �ࢥ�� ᮪��
	 */
	private void closeAll() {
		try {
			server.close();
			
			// ��ॡ�� ��� Connection � �맮� ��⮤� close() ��� �������. ����
			// synchronized {} ����室�� ��� �ࠢ��쭮�� ����㯠 � ����� �����
			// �� ࠧ��� ��⥩
			synchronized(connections) {
				Iterator<Connection> iter = connections.iterator();
				while(iter.hasNext()) {
					((Connection) iter.next()).close();
				}
			}
		} catch (Exception e) {
			System.err.println("��⮪� �� �뫨 �������!");
		}
	}

	/**
	 * ����� ᮤ�ন� �����, �⭮��騥�� � �����⭮�� ������祭��:
	 * <ul>
	 * <li>��� ���짮��⥫�</li>
	 * <li>᮪��</li>
	 * <li>�室��� ��⮪ BufferedReader</li>
	 * <li>��室��� ��⮪ PrintWriter</li>
	 * </ul>
	 * ������� Thread � � ��⮤� run() ����砥� ���ଠ�� �� ���짮��⥫� �
	 * ����뫠�� �� ��㣨�
	 * 
	 *
	 */
	private class Connection extends Thread {
		private BufferedReader in;
		private PrintWriter out;
		private Socket socket;
	
		private String name = "";
	
		/**
		 * ���樠������� ���� ��ꥪ� � ����砥� ��� ���짮��⥫�
		 * 
		 *param socket
		 *            ᮪��, ����祭�� �� server.accept()
		 */
		public Connection(Socket socket) {
			this.socket = socket;
	
			try {
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
	
			} catch (IOException e) {
				e.printStackTrace();
				close();
			}
		}
	
		/**
		 * ����訢��� ��� ���짮��⥫� � ������� �� ���� ᮮ�饭��. ��
		 * ����祭�� ������� ᮮ�饭��, ��� ����� � ������ ���짮��⥫�
		 * ����뫠���� �ᥬ ��⠫��.
		 */
		@Override
		public void run() {
			try {
				name = in.readLine();
				// ��ࠢ�塞 �ᥬ �����⠬ ᮮ�饭�� � ⮬, �� ���� ���� ���짮��⥫�
				synchronized(connections) {
					Iterator<Connection> iter = connections.iterator();
					while(iter.hasNext()) {
						((Connection) iter.next()).out.println(name + " cames now");
					}
				}
				
				String str = "";
				while (true) {
					str = in.readLine();
					if(str.equals("exit")) break;
					
					// ��ࠢ�塞 �ᥬ �����⠬ ��।��� ᮮ�饭��
					synchronized(connections) {
						Iterator<Connection> iter = connections.iterator();
						while(iter.hasNext()) {
							((Connection) iter.next()).out.println(name + ": " + str);
						}
					}
				}
				
				synchronized(connections) {
					Iterator<Connection> iter = connections.iterator();
					while(iter.hasNext()) {
						((Connection) iter.next()).out.println(name + " has left");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}
	
		/**
		 * ����뢠�� �室��� � ��室��� ��⮪� � ᮪��
		 */
		public void close() {
			try {
				in.close();
				out.close();
				socket.close();
	
				// �᫨ ����� �� ��⠫��� ᮥ�������, ����뢠�� ���, �� ���� �
				// �����蠥� ࠡ��� �ࢥ�
				connections.remove(this);
				if (connections.size() == 0) {
					Server.this.closeAll();
					System.exit(0);
				}
			} catch (Exception e) {
				System.err.println("��⮪� �� �뫨 �������!");
			}
		}
	}
}

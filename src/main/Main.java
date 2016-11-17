package main;

import client.Client;
import server.Server;

import java.util.Scanner;

/**
 * ���⮢�� �窠 �ணࠬ��. ����ন� �����⢥��� ��⮤ main
 * 
 *
 */
public class Main {

	/**
	 * ���訢��� ���짮��⥫� � ०��� ࠡ��� (�ࢥ� ��� ������) � ��।���
	 * �ࠢ����� ᮮ⢥�����饬� ������
	 *
	 * param args
	 *            ��ࠬ���� ��������� ��ப�
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		System.out.println("�������� �ணࠬ�� � ०��� �ࢥ� ��� ������? (S(erver) / C(lient))");
		while (true) {
			char answer = Character.toLowerCase(in.nextLine().charAt(0));
			if (answer == 's') {
				new Server();
				break;
			} else if (answer == 'c') {
				new Client();
				break;
			} else {
				System.out.println("�����४�� ����. ������.");
			}
		}
	}

}

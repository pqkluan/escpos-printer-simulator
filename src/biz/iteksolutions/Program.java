package biz.iteksolutions;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;

public class Program {

		private static final int DEFAULT_PORT = 9100;
		private static final String DEFAULT_PATH = "";
		private static final int DEFAULT_FILE_SIZE = 200;

		private static IPrinterOutput createPrinterOutput(boolean isGUI, String outputFilePath, int outputFileSize) {
				// Write the output to file
				if (!isGUI)
						return new FileOutputImpl(outputFilePath, outputFileSize);

				// Display GUI
				PrinterOutputImpl pPanel = new PrinterOutputImpl();
				JFrame frame = new JFrame("ESC/POS Printer Simulator");
				frame.setContentPane(pPanel);
				frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
				frame.pack();
				frame.setVisible(true);
				return pPanel;
		}

		/**
		 * Main app
		 * 
		 * @param args
		 *        args[0] - port - require if use file in args[1] 
		 *        args[1] - optional - file path - output to file only 
		 *        args[2] - optional - file size KB
		 * @throws IOException
		 */
		public static void main(String[] args) throws IOException {
				// The number of times this printer was connected
				int clientNumber = 1;

				// Params pass when run the jar file
				int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
				String path = args.length > 1 ? path = args[1] : DEFAULT_PATH;
				int fileSize = args.length > 2 ? Integer.parseInt(args[2]) : DEFAULT_FILE_SIZE;

				IPrinterOutput output = createPrinterOutput(path.isEmpty(), path, fileSize);
				ServerSocket listener = new ServerSocket(port); // Pointer for closing port listener

				System.out.println("Print Emulator start with these params");
				System.out.println("SOCKET PORT: " + port);
				System.out.println("OUTPUT FILE PATH: " + path);
				System.out.println("OUTPUT FILE SIZE: " + fileSize);

				try {
						while (true) {
								PrinterServer printerServer = new PrinterServer(output, listener.accept(), clientNumber++);
								printerServer.start();
						}
				} finally {
						listener.close();
				}
		}
}

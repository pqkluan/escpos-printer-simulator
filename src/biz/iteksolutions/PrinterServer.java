package biz.iteksolutions;

import biz.iteksolutions.escpos.parser.*;

import java.io.*;
import java.net.Socket;

public class PrinterServer extends Thread {

		private IPrinterOutput display;
		private Socket socket;
		private int clientNumber;

		// Constructor
		public PrinterServer(IPrinterOutput display, Socket socket, int clientNumber) {
				this.display = display;
				this.socket = socket;
				this.clientNumber = clientNumber;
				log("Socket no." + clientNumber + " opened");
		}

		@Override
		public void run() {
				try {
						BufferedReader in = new BufferedReader(
										new InputStreamReader(socket.getInputStream(), "GBK")); // GBK charset is for textChinese support

						// Get messages from the client, line by line
						// And send these messages to the output
						String sCurrentLine;
						while ((sCurrentLine = in.readLine()) != null) {
								log(sCurrentLine);

								Printer printer = new Printer();
								for (int i = 0; i < sCurrentLine.length(); i++) {
										// System.out.println(i + ": " + input.charAt(i));
										printer.addChar(sCurrentLine.charAt(i));
								}

								// Explicitly add new line here because in.readLine ignore LF
								printer.getCommands().add(new LineFeedCommand());

								// handle QR print - QR command content is 4th
								int qrCount = 0;
								for (Command cmd : printer.getCommands()) {
										if (cmd instanceof PrintQRCommand) {
												qrCount++;

												if (qrCount == 4)
														((PrintQRCommand) cmd).setContentFlag(true);

												if (qrCount == 5)
														qrCount = 0; // reset value
										}
								}

								if (printer.getCommands().size() > 0) {
										for (Command obj : printer.getCommands()) {
												if (obj instanceof IContentOutput) {
														IContentOutput container = (IContentOutput) obj;
														display.setText(container.getText());
												}
										}
								}
						}
				} catch (IOException e) {
						log("Error handling client# " + clientNumber + ": " + e);
				} finally {
						try {
								socket.close();
						} catch (IOException e) {
								log("Couldn't close a socket, what's going on?");
						}
						log("Socket no." + clientNumber + " closed");
				}
		}

		private void log(String message) {
				System.out.println(message);
		}
}

package main;

import java.util.List;
import org.w3c.dom.NodeList;

public class Ybus {

	public boolean ybus(int[][] Termindex, int[][] Connindex, List<String> Term_CondEqui_List, NodeList TermList,
			NodeList ConnNodeList, NodeList BusbarSectList, double syncMach_ratedS, double[][] LinearShuntCom_bgUs,
			double[][] rxbglv, double[][] PowetTransEnd_rxvgb) {

		// give Two terminal devices(breaker, transformer and AC line) connected tag //
		// if two terminals are connected to breaker 5//
		// Then we insert another terminal number into <Termindex>'s 4th column //
		// for one terminal equipment 0 tag in the same column//

		// initialize the 4th column//
		for (int i = 0; i < TermList.getLength(); i++) {
			Termindex[i][3] = TermList.getLength() + 1;
		}

		String conduct_rdfID_temp1, conduct_rdfID_temp2;
		int conduct_equip_type;
		for (int i = 0; i < TermList.getLength(); i++) {
			// if it's a breaker---> 3th column = 5
			// and it has no matched terminal (to save calculation resource)
			if (((Termindex[i][2] == 5) | (Termindex[i][2] == 3) | (Termindex[i][2] == 4))
					&& (Termindex[i][3] == TermList.getLength() + 1)) {
				conduct_equip_type = Termindex[i][2];
				conduct_rdfID_temp1 = Term_CondEqui_List.get(i);
				for (int j = i; j < TermList.getLength(); j++) {
					if ((Termindex[i][2] == conduct_equip_type) && (Termindex[j][3] == TermList.getLength() + 1)) {
						conduct_rdfID_temp2 = Term_CondEqui_List.get(j);
						if (conduct_rdfID_temp1.equals(conduct_rdfID_temp2)) {
							Termindex[i][3] = j;
							Termindex[j][3] = i;
						}
					}
				}
			} else {
				if (Termindex[i][3] == TermList.getLength() + 1) {
					Termindex[i][3] = i;
				}
			}
		}

		int bus_num;
		int n2 = 0;
		// initialize
		for (int i = 0; i < TermList.getLength(); i++) {
			Termindex[i][6] = BusbarSectList.getLength() + 1;
		}

		for (int i = 0; i < TermList.getLength(); i++) {
			if (Termindex[i][2] == 1) {
				bus_num = Termindex[i][4];
				for (int j = 0; j < TermList.getLength(); j++) {
					if (Termindex[i][1] == Termindex[j][1]) {
						Termindex[j][6] = bus_num;
						//System.out.println("find one" + j + "->" + bus_num);
						n2++;
					}
				}
			}
		}
		
		/*
		// Output terminal connection index//
		for (int i = 0; i < ConnNodeList.getLength(); i++) {
			System.out.println(" ConnecNode:" + Connindex[i][0] + " Busbarsection" + Connindex[i][1]);
		}
		*/
		
		// Flags to stop the loop
		int terminal_busbarFlag = 0, bus_i = 0, bus_j = 0;
		int totalFlag = 0, totalFlag_temp = 1, CEtype_temp = 0;

		// CN CE Te records
		int current_Te = 0, next_Te = 0;// , current_Te_temp = 0;
		int CEnumber;

		// Impedance and resistance, complex number declaration
		double r_ij, x_ij, b_ij, g_ii, g_jj, b_ii, b_jj, g_ij, z_base, u_base, s_base;
		ComplexNumber yy = new ComplexNumber(0.0, 0.0);
		s_base = syncMach_ratedS;
		int ybus_dimen = BusbarSectList.getLength();
		ComplexNumber[][] YBUS = new ComplexNumber[ybus_dimen][ybus_dimen];
		ComplexNumber z_ij_temp_ii = new ComplexNumber(0, 0);
		ComplexNumber z_ij_temp_jj = new ComplexNumber(0, 0);
		ComplexNumber z_ij_temp_ij = new ComplexNumber(0, 0);
		ComplexNumber y_ij_temp_ii = new ComplexNumber(0, 0);
		ComplexNumber y_ij_temp_jj = new ComplexNumber(0, 0);

		// YBus matrix
		for (int i = 0; i < ybus_dimen; i++) {
			for (int j = 0; j < ybus_dimen; j++) {
				YBUS[i][j] = yy;
			}
		}

		totalFlag_temp = 1;
		while (totalFlag == 0) {
			for (int i = 0; i < BusbarSectList.getLength(); i++) {
				bus_i = i;
				System.out.println("busbar No." + i + ".");
				for (int j = 0; j < TermList.getLength(); j++) {
					CEnumber = Termindex[j][4];

					// if it's connected to this bus
					if (Termindex[j][6] == i) {
						System.out.println("Start from a new terminal-> " + j);

						// if it's a single port terminal
						if (Termindex[j][3] == j) {

							switch (Termindex[j][2]) {
							case 1:
								System.out.println("This is the NO." + CEnumber + " busbarsection");
								Termindex[j][5] = 1;
								break;
							case 2:
								System.out.println("This is the NO." + CEnumber + " synchronous machine");
								Termindex[j][5] = 1;
								break;
							case 6:
								System.out.println("This is the NO." + CEnumber + " linear shunt compensator");
								Termindex[j][5] = 1;
								double sect = LinearShuntCom_bgUs[CEnumber][3];
								z_base = LinearShuntCom_bgUs[CEnumber][2] * LinearShuntCom_bgUs[CEnumber][2] / s_base;
								b_ij = LinearShuntCom_bgUs[CEnumber][0] * sect * z_base;
								g_ij = LinearShuntCom_bgUs[CEnumber][1] * sect * z_base;
								ComplexNumber y_ij_ACline = new ComplexNumber(g_ij, b_ij);
								YBUS[bus_i][bus_i] = yy.addcomplex(YBUS[bus_i][bus_i], y_ij_ACline);
								break;
							case 7:
								System.out.println("This is the NO." + CEnumber + " energy consumer");
								Termindex[j][5] = 1;
								break;

							}
						}
						// else -> it's a two port terminal
						else {
							if (Termindex[j][5] == 0) {
								current_Te = j;
								next_Te = Termindex[current_Te][3];
								terminal_busbarFlag = 0;
								// expand it until another terminal reach to another busbar

								while (terminal_busbarFlag == 0) {
									CEnumber = Termindex[current_Te][4];

									switch (Termindex[current_Te][2]) {
									case 3:
										CEtype_temp = Termindex[current_Te][2];

										// impedance_length, rxbglv
										r_ij = rxbglv[CEnumber][0];
										x_ij = rxbglv[CEnumber][1];
										b_ij = rxbglv[CEnumber][2];
										g_ij = rxbglv[CEnumber][3];

										u_base = rxbglv[CEnumber][5];
										z_base = u_base * u_base / s_base;
										r_ij = r_ij * rxbglv[CEnumber][4] / z_base;
										x_ij = x_ij * rxbglv[CEnumber][4] / z_base;
										b_ii = b_ij * rxbglv[CEnumber][4] * z_base / 2;
										g_ii = g_ij * rxbglv[CEnumber][4] * z_base / 2;
										
										z_ij_temp_ii = ComplexNumber.addto(z_ij_temp_ii, r_ij, x_ij);
										z_ij_temp_jj = ComplexNumber.addto(z_ij_temp_jj, r_ij, x_ij);
										z_ij_temp_ij = ComplexNumber.addto(z_ij_temp_ij, r_ij, x_ij);
										y_ij_temp_ii = ComplexNumber.addto(y_ij_temp_ii, g_ii, b_ii);
										y_ij_temp_jj = ComplexNumber.addto(y_ij_temp_jj, g_ii, b_ii);
										
										Termindex[current_Te][5] = 1;
										Termindex[next_Te][5] = 1;
										break;
									case 4:
										CEtype_temp = Termindex[current_Te][2];
										
										// bus_i end
										u_base = PowetTransEnd_rxvgb[Termindex[current_Te][7]][2];
										z_base = u_base * u_base / s_base;

										r_ij = PowetTransEnd_rxvgb[Termindex[current_Te][7]][0];
										x_ij = PowetTransEnd_rxvgb[Termindex[current_Te][7]][1];
										g_ii = PowetTransEnd_rxvgb[Termindex[current_Te][7]][3];
										b_ii = PowetTransEnd_rxvgb[Termindex[current_Te][7]][4];
										
										r_ij = r_ij / z_base;
										x_ij = x_ij / z_base;
										g_ii = g_ii * z_base;
										b_ii = b_ii * z_base;

										z_ij_temp_ii = ComplexNumber.addto(z_ij_temp_ii, r_ij, x_ij);
										z_ij_temp_jj = ComplexNumber.addto(z_ij_temp_jj, r_ij, x_ij);
										z_ij_temp_ij = ComplexNumber.addto(z_ij_temp_ij, r_ij, x_ij);
										y_ij_temp_ii = ComplexNumber.addto(y_ij_temp_ii, g_ii, b_ii);
										
										// bus_j end
										u_base = PowetTransEnd_rxvgb[Termindex[next_Te][7]][2];
										z_base = u_base * u_base / s_base;

										r_ij = PowetTransEnd_rxvgb[Termindex[next_Te][7]][0];
										x_ij = PowetTransEnd_rxvgb[Termindex[next_Te][7]][1];
										g_jj = PowetTransEnd_rxvgb[Termindex[next_Te][7]][3];
										b_jj = PowetTransEnd_rxvgb[Termindex[next_Te][7]][4];
										
										r_ij = r_ij / z_base;
										x_ij = x_ij / z_base;
										g_jj = g_jj * z_base;
										b_jj = b_jj * z_base;

										z_ij_temp_ii = ComplexNumber.addto(z_ij_temp_ii, r_ij, x_ij);
										z_ij_temp_jj = ComplexNumber.addto(z_ij_temp_jj, r_ij, x_ij);
										z_ij_temp_ij = ComplexNumber.addto(z_ij_temp_ij, r_ij, x_ij);
										y_ij_temp_jj = ComplexNumber.addto(y_ij_temp_jj, g_jj, b_jj);
										
										Termindex[current_Te][5] = 1;
										Termindex[next_Te][5] = 1;
										break;
									case 5:
										System.out.println("This is the NO." + CEnumber + " breaker");

										Termindex[current_Te][5] = 1;
										Termindex[next_Te][5] = 1;
										break;
									}
									if (Termindex[next_Te][6] < BusbarSectList.getLength()) {
										bus_j = Termindex[next_Te][6];
										if ((CEtype_temp == 3) | (CEtype_temp == 4)) {

											ComplexNumber y_ii = addoneline(yy.real(z_ij_temp_ii),
													yy.imag(z_ij_temp_ii));
											ComplexNumber y_jj = addoneline(yy.real(z_ij_temp_jj),
													yy.imag(z_ij_temp_jj));
											ComplexNumber y_ij = addoneline(-yy.real(z_ij_temp_ij),
													-yy.imag(z_ij_temp_ij));
											ComplexNumber y_ii2 = y_ij_temp_ii;
											ComplexNumber y_jj2 = y_ij_temp_jj;

											YBUS[bus_i][bus_i] = ComplexNumber.add(YBUS[bus_i][bus_i], y_ii);
											YBUS[bus_j][bus_j] = ComplexNumber.add(YBUS[bus_j][bus_j], y_jj);
											YBUS[bus_i][bus_j] = ComplexNumber.add(YBUS[bus_i][bus_j], y_ij);
											YBUS[bus_j][bus_i] = ComplexNumber.add(YBUS[bus_j][bus_i], y_ij);

											// YBUS[bus_j][bus_i] = yy.add(YBUS[bus_j][bus_i], y_ij);
											YBUS[bus_i][bus_i] = ComplexNumber.add(YBUS[bus_j][bus_j], y_ii2);
											YBUS[bus_j][bus_j] = ComplexNumber.add(YBUS[bus_i][bus_i], y_jj2);
											
										}

										terminal_busbarFlag = 1;
										CEtype_temp = 0;
										z_ij_temp_ii = new ComplexNumber(0.0, 0.0);
										z_ij_temp_jj = new ComplexNumber(0.0, 0.0);
										z_ij_temp_ij = new ComplexNumber(0.0, 0.0);
										y_ij_temp_ii = new ComplexNumber(0.0, 0.0);
										y_ij_temp_jj = new ComplexNumber(0.0, 0.0);
									} else {
										for (int k2 = 0; k2 < TermList.getLength(); k2++) {
											if ((Termindex[k2][1] == Termindex[next_Te][1])
													&& (Termindex[k2][5] == 0)) {
												System.out.println(
														"Current_Te will increase from " + current_Te + " to " + k2);
												current_Te = k2;
												next_Te = Termindex[current_Te][3];
												break;

											}
										}
									}
								}
							}
						}
					}
				}
			}
			totalFlag_temp = 1;
			for (int i = 0; i < TermList.getLength(); i++) {
				totalFlag_temp = totalFlag_temp * Termindex[i][5];
			}
			totalFlag = totalFlag_temp;
		}
		// YBus matrix
		System.out.println("Objects: PT, AC line, power transformer and breaker are taken into consideration...");
		System.out.println("Y bus matrix is generating...");
		
		for (int i = 0; i < ybus_dimen; i++) {
			for (int j = 0; j < ybus_dimen; j++) {
				System.out.print(ComplexNumber.set(YBUS[i][j])+"\t"+"\t"+"\t");
			}
			System.out.println("");
		}
		return true;
	}

	public static ComplexNumber addoneline(double r, double x) {
		double denominator = r * r + x * x;
		double real = r / denominator;
		double imag = -x / denominator;
		ComplexNumber y = new ComplexNumber(real, imag);
		return y;
	}

}

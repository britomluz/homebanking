package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEnconder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository,
									  ContactRepository contactRepository,
									  MovementRepository movementRepository,
									  PayRepository payRepository) {
		return (args) -> {

			Client melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEnconder.encode("melba123"), "https://www.marketingdirecto.com/wp-content/uploads/2020/03/dia-de-la-mujer-monica-moro.png", ClientType.ADMIN);
			Client luz = new Client("Luz", "Brito","britomluz@gmail.com", passwordEnconder.encode("luz123"),"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcROXQAAlpugoyysL2by-ZDUVND2ojwYETCQsA&usqp=CAU", ClientType.CLIENT );
			Client felipe = new Client("Felipe", "Ruiz","felipr@gmail.com", passwordEnconder.encode("felipe123"),"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcROXQAAlpugoyysL2by-ZDUVND2ojwYETCQsA&usqp=CAU", ClientType.CLIENT );
			Client vanesa = new Client("Vanesa", "Cortes","vanecortes@gmail.com", passwordEnconder.encode("vanesa123"),"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcROXQAAlpugoyysL2by-ZDUVND2ojwYETCQsA&usqp=CAU", ClientType.CLIENT );
			Client diego = new Client("Diego", "de la Vega","deiegodelavega@gmail.com", passwordEnconder.encode("diego123"),"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcROXQAAlpugoyysL2by-ZDUVND2ojwYETCQsA&usqp=CAU", ClientType.CLIENT );
			Client luis = new Client("Luis", "Perez","luisp@gmail.com", passwordEnconder.encode("luisp123"),"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcROXQAAlpugoyysL2by-ZDUVND2ojwYETCQsA&usqp=CAU", ClientType.CLIENT );



			Account vin001 = new Account("VIN00000001", LocalDateTime.now().minusMonths(3).plusDays(9), AccountType.CAJADEAHORRO, 5000,melba);
			Account vin002 = new Account("VIN00000002", LocalDateTime.now().minusMonths(2).minusDays(6).minusHours(3), AccountType.CUENTACORRIENTE, 8500,melba);

			Account vin003 = new Account("VIN00000003", LocalDateTime.now(), AccountType.CUENTACORRIENTE, 8000,luz);
			Account vin004 = new Account("VIN00000004", LocalDateTime.now().plusDays(1), AccountType.CAJADEAHORRO, 5000,luz);

			Account vin005 = new Account("TAD92365563", LocalDateTime.now(), AccountType.CUENTACORRIENTE, 8000, felipe);
			Account vin006 = new Account("VIN23658548", LocalDateTime.now().plusDays(1), AccountType.CAJADEAHORRO, 5000,vanesa);
			Account vin007 = new Account("FUC23658742", LocalDateTime.now(), AccountType.CUENTACORRIENTE, 8000,diego);
			Account vin008 = new Account("CAB89638742", LocalDateTime.now().plusDays(1), AccountType.CAJADEAHORRO, 5000,luis);


			Transaction transf0001 = new Transaction(TransactionType.CREDITO, 2000, "Bank Santander - De cuenta Nº VIN00000008", LocalDateTime.now().plusDays(1).minusYears(3));
			Transaction transf0002 = new Transaction(TransactionType.DEBITO, 3000, "J. P. A. - A cuenta Nº VIN00000047", LocalDateTime.now().plusDays(3).minusYears(2));

			Transaction transf0005 = new Transaction(TransactionType.CREDITO, 5200, "Alta Via SRL - De cuenta Nº VIN00000012", LocalDateTime.now().minusMonths(9).plusDays(6).minusYears(3));
			Transaction transf0006 = new Transaction(TransactionType.DEBITO, 13000, "Reebok - A cuenta Nº VIN00000011", LocalDateTime.now().minusMonths(5).minusDays(4).minusYears(2));
			Transaction transf0007 = new Transaction(TransactionType.CREDITO, 9700, "Ivana Morales - De cuenta Nº VIN00000005", LocalDateTime.now().minusYears(1).minusDays(7).minusYears(1));
			Transaction transf0008 = new Transaction(TransactionType.DEBITO, 8400, "Supermercado - A cuenta Nº VIN00000009" , LocalDateTime.now().minusMonths(3).minusDays(10).minusYears(1));
			Transaction transf0009 = new Transaction(TransactionType.CREDITO, 2900, "Fernando Pais - De cuenta Nº VIN00000005", LocalDateTime.now().minusMonths(2).plusDays(8).minusYears(1));
			Transaction transf0010 = new Transaction(TransactionType.DEBITO, 30000, "Banco Macro - A cuenta Nº VIN00000006", LocalDateTime.now().minusMonths(4).minusDays(5).minusYears(1));
			Transaction transf0011 = new Transaction(TransactionType.CREDITO, 6500, "Mezcla S.A - De cuenta Nº VIN00000008", LocalDateTime.now().minusMonths(2).minusDays(2).minusYears(1));
			Transaction transf0012 = new Transaction(TransactionType.DEBITO, 23000, "Federada Salud - A cuenta Nº VIN00000005", LocalDateTime.now().minusMonths(7).minusDays(15));
			Transaction transf0013 = new Transaction(TransactionType.CREDITO, 7600, "S.T.V - De cuenta Nº VIN00000006", LocalDateTime.now().plusDays(8));
			Transaction transf0014 = new Transaction(TransactionType.DEBITO, 5800, "Libertad S.A. - A cuenta Nº VIN00000006", LocalDateTime.now().minusMonths(3).plusDays(6));
			Transaction transf0015 = new Transaction(TransactionType.CREDITO, 32000, "Mercado Pago De cuenta VIN00000001", LocalDateTime.now().minusMonths(4).minusDays(3));
			Transaction transf0016 = new Transaction(TransactionType.CREDITO, 9200, "Devolucion dinero - De cuenta Nº Nº VIN00000006", LocalDateTime.now().minusDays(5));

			Transaction transf0019 = new Transaction(TransactionType.CREDITO, 7100, "Libertad S.A. - De cuenta Nº VIN00000018", LocalDateTime.now().minusMonths(2).plusDays(6));
			Transaction transf0020 = new Transaction(TransactionType.DEBITO, 3800, "Tienda Mia - A cuenta Nº VIN00000005", LocalDateTime.now().minusMonths(1).minusDays(3));
			Transaction transf0021 = new Transaction(TransactionType.DEBITO, 1290, "Garbarino - A cuenta Nº Nº VIN00000006", LocalDateTime.now().minusDays(13));
			Transaction transf0022 = new Transaction(TransactionType.DEBITO, 900, "Supermercado - A cuenta Nº VIN00000006", LocalDateTime.now().minusMonths(3).plusDays(9));
			Transaction transf0023 = new Transaction(TransactionType.CREDITO, 6200, "Honorarios - De cuenta Nº VIN00000026", LocalDateTime.now().minusMonths(5).minusDays(15));
			Transaction transf0024 = new Transaction(TransactionType.DEBITO, 12500, "Fravega - A cuenta Nº VIN00000008", LocalDateTime.now().minusDays(2));

			Transaction transf0017 = new Transaction(TransactionType.CREDITO, 1080, "Mercado Pago - De cuenta Nº VIN00000007", LocalDateTime.now().minusMonths(3).plusDays(12));
			Transaction transf0018 = new Transaction(TransactionType.CREDITO, 100, "Marcos Acosta - De cuenta Nº VIN00000006", LocalDateTime.now().plusDays(8));


			Transaction transf0003 = new Transaction(TransactionType.DEBITO, 1000, "New Sports - A cuenta Nº VIN00000007", LocalDateTime.now().plusDays(3).minusYears(3));
			Transaction transf0004 = new Transaction(TransactionType.CREDITO, 1000, "Disco S.A. - De cuenta Nº VIN00000005", LocalDateTime.now().minusYears(3));

			var paymentsHipotecario = List.of(12,24,36,48,60);
			var paymentsPersonal = List.of(6,12,24);
			var paymentsAutomotriz = List.of(6,12,24,36);



			Loan Hipotecario = new Loan("Hipotecario", 500000d, paymentsHipotecario, 35);
			Loan Personal = new Loan("Personal", 100000d, paymentsPersonal, 20);
			Loan Automotriz = new Loan("Automotriz", 300000d, paymentsAutomotriz, 30);

			ClientLoan l0001 = new ClientLoan(400000d, Hipotecario.getPayments().get(4), Hipotecario, melba);
			ClientLoan l0002 = new ClientLoan(50000d, Personal.getPayments().get(1), Personal, melba);
			ClientLoan l0003 = new ClientLoan(100000d, Personal.getPayments().get(2), Personal, luz);
			ClientLoan l0004 = new ClientLoan(200000d, Automotriz.getPayments().get(3), Automotriz, luz);


			Card debitGold = new Card(CardType.DEBITO, CardColor.GOLD, melba.getFirstName().toUpperCase() + " " + melba.getLastName().toUpperCase(), "5287 6214 8516 8953", 470, LocalDateTime.now().minusDays(20), LocalDateTime.now().plusYears(5), melba);
			Card creditTitanium = new Card(CardType.CREDITO, CardColor.TITANIUM, melba.getFirstName().toUpperCase() + " " + melba.getLastName().toUpperCase(), "5427 0219 6119 2413", 174, LocalDateTime.now(), LocalDateTime.now().plusYears(5).minusMonths(6), melba);
			Card creditSilver = new Card(CardType.CREDITO, CardColor.SILVER, luz.getFirstName().toUpperCase() + " " + luz.getLastName().toUpperCase(), "5255 0229 3315 1982", 214, LocalDateTime.now(), LocalDateTime.now().plusYears(5), luz);

			Pay pay01 = new Pay(LocalDateTime.now().minusMonths(8), "Pago de factura de luz", 4700, 6, 4700/6, creditTitanium);
			Pay pay02 = new Pay(LocalDateTime.now().minusMonths(8), "Compra en Supermercado Dia", 1500, 1, 1500, debitGold);
			Pay pay03 = new Pay(LocalDateTime.now().minusMonths(8), "Compra de cafetera", 10900, 12,10900/12, creditTitanium);
			Pay pay04 = new Pay(LocalDateTime.now().minusMonths(8), "Compra de heladera", 28000, 24,28000/24, creditTitanium);
			Pay pay05 = new Pay(LocalDateTime.now().minusMonths(8), "Pago de servicio de agua", 3500, 1,3500, debitGold);

			Contact contact1 = new Contact("Felipe Ruiz", "TAD92365563", ContactType.NOFAV, melba);
			Contact contact2 = new Contact("Vanesa Cortes", "VIN23658548",ContactType.FAV, melba);
			Contact contact3 = new Contact("Luz Brito", "VIN00000003", ContactType.FAV, melba);
			Contact contact4 = new Contact("Diego de la Vega", "FUC23658742", ContactType.FAV , melba);
			Contact contact5 = new Contact("Luis Perez", "CAB89638742",ContactType.FAV, melba);
			Contact contact6 = new Contact("Marcos Vargas", "VIN63867492",ContactType.FAV, luz);
			Contact contact7 = new Contact("Amanda Benitez", "TAD51538742",ContactType.FAV, luz);

			Movement movement5 = new Movement("Extracción: $1500", LocalDateTime.now().minusDays(2).minusHours(4).minusMinutes(47), melba);
			Movement movement4 = new Movement("Nueva cuenta creada con éxito", LocalDateTime.now().minusDays(6).minusHours(10), melba);
			Movement movement3 = new Movement("Extracción: $2500", LocalDateTime.now().minusDays(8).minusHours(6).minusMinutes(21), melba);
			Movement movement2 = new Movement("Préstamo Personal rechazado. Motivo: 'Ud. ya posee un préstamo personal.'", LocalDateTime.now().minusDays(10).minusHours(11).minusMinutes(18), melba);
			Movement movement1 = new Movement("Solicitud de tarjeta rechazada. Motivo: 'Ud. ya posee una tarjeta de CREDITO TITANIUM'", LocalDateTime.now().minusDays(10).minusHours(8).minusMinutes(53), melba);

			luz.addAccount(vin003); luz.addAccount(vin004);
			melba.addAccount(vin001); melba.addAccount(vin002);

			vin001.addTransaction(transf0001); vin001.addTransaction(transf0002);
			vin001.addTransaction(transf0005);vin001.addTransaction(transf0008);
			vin001.addTransaction(transf0010);vin001.addTransaction(transf0012);
			vin001.addTransaction(transf0015);vin001.addTransaction(transf0017);vin001.addTransaction(transf0018);
			vin001.addTransaction(transf0020);vin001.addTransaction(transf0022);vin001.addTransaction(transf0024);

			vin002.addTransaction(transf0003); vin002.addTransaction(transf0006);
			vin002.addTransaction(transf0009);vin002.addTransaction(transf0007);
			vin002.addTransaction(transf0011);vin002.addTransaction(transf0013);
			vin002.addTransaction(transf0014);vin002.addTransaction(transf0016);
			vin002.addTransaction(transf0019);vin002.addTransaction(transf0021);vin002.addTransaction(transf0023);

			vin003.addTransaction(transf0004);

			//melba.addClientLoan(l0001);
			//loanHipotecario.addClientLoan(l0001);

			melba.addCard(debitGold); melba.addCard(creditTitanium);
			luz.addCard(creditSilver);

			vin001.addCard(debitGold);
			//debitGold.addAccount(vin001);



			clientRepository.save(melba);
			clientRepository.save(luz);

			accountRepository.save(vin001);
			accountRepository.save(vin002);

			accountRepository.save(vin003);
			accountRepository.save(vin004);

			transactionRepository.save(transf0001);
			transactionRepository.save(transf0002);
			transactionRepository.save(transf0003);
			transactionRepository.save(transf0004);

			transactionRepository.save(transf0005);
			transactionRepository.save(transf0006);
			transactionRepository.save(transf0007);
			transactionRepository.save(transf0008);
			transactionRepository.save(transf0009);
			transactionRepository.save(transf0010);
			transactionRepository.save(transf0011);
			transactionRepository.save(transf0012);
			transactionRepository.save(transf0013);
			transactionRepository.save(transf0014);
			transactionRepository.save(transf0015);
			transactionRepository.save(transf0016);
			transactionRepository.save(transf0017);
			transactionRepository.save(transf0018);
			transactionRepository.save(transf0019);
			transactionRepository.save(transf0020);
			transactionRepository.save(transf0021);
			transactionRepository.save(transf0022);
			transactionRepository.save(transf0023);
			transactionRepository.save(transf0024);


			loanRepository.save(Hipotecario);
			loanRepository.save(Personal);
			loanRepository.save(Automotriz);

			clientLoanRepository.save(l0001);
			clientLoanRepository.save(l0002);
			clientLoanRepository.save(l0003);
			clientLoanRepository.save(l0004);

			cardRepository.save(debitGold);
			cardRepository.save(creditTitanium);
			cardRepository.save(creditSilver);


			contactRepository.save(contact1); contactRepository.save(contact2);
			contactRepository.save(contact3); contactRepository.save(contact4);
			contactRepository.save(contact5); contactRepository.save(contact6);
			contactRepository.save(contact7);

			movementRepository.save(movement1); movementRepository.save(movement2);
			movementRepository.save(movement3); movementRepository.save(movement4);
			movementRepository.save(movement5);


			payRepository.save(pay01); payRepository.save(pay02);
			payRepository.save(pay03); payRepository.save(pay04);
			payRepository.save(pay05);


		};
	}
}

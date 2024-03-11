package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.GenerateRandomNum;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
class HomeBankingApplicationTests {

	@Test
	void contextLoads() {
	}

	@RepeatedTest(10)
	public void testRandomNumberCard() {
		GenerateRandomNum generateRandomNum = new GenerateRandomNum();

		String card = generateRandomNum.getRandomNumberCard();
		assertThat(card.length(), equalTo(19));
	}
	@RepeatedTest(10)
	public void testRandomNumberCVV() {
		GenerateRandomNum generateRandomNum = new GenerateRandomNum();

		String cvv = generateRandomNum.getRandomNumberCVV();
		assertThat(cvv.length(), equalTo(3));
	}

	@RepeatedTest(10)
	public void testRandomNumberAccount() {
		GenerateRandomNum generateRandomNum = new GenerateRandomNum();

		String account = generateRandomNum.getRandomNumberAccount();
		assertThat(account.length(), equalTo(12));
	}
}

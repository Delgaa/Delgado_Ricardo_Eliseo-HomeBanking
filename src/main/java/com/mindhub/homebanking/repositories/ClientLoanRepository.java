package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {
    Boolean existsLoanByLoanAndClient(Loan loan, Client client);

    List<ClientLoan> findByClient(Client testClient);

}

package mintos.homework

import mintos.homework.domain.TransactionStatus
import mintos.homework.dto.CurrencyExchangeRateDTO
import mintos.homework.dto.FundTransactionRequestDTO
import mintos.homework.exception.ApiException
import mintos.homework.service.ClientAccountService
import mintos.homework.service.ClientTransactionService
import mintos.homework.service.CurrencyExchangeRateService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired

class ClientTransactionIntegrationSpec extends DatabaseSpecTemplate {

    @Autowired
    ClientTransactionService clientTransactionService
    @Autowired
    ClientAccountService clientAccountService
    
    @SpringBean
    CurrencyExchangeRateService currencyExchangeRateService = Mock()
    
    def 'test executeClientFundTransaction() - happy path'() {
        given:
        def fromAccount = clientAccountService.listClientAccounts(UUID.fromString('e9e187d7-2ac3-4a71-bb59-5ca84c4a7d66'))[0] // USD account
        def toAccount = clientAccountService.listClientAccounts(UUID.fromString('b1fd0df8-82f2-48f5-91ea-93b7b1d7dab4'))[1]   // GBP account
        def request = new FundTransactionRequestDTO(fromAccount.id(), toAccount.id(), 1.0G, "GBP")
        
        when: 'check before amount and execute transaction'
        fromAccount.balance() == 1000.00
        toAccount.balance() == 750.25
        
        def result = clientTransactionService.executeClientFundTransaction(request)
        
        then: 'check interactions with external service'
        1 * currencyExchangeRateService.getCurrencyExchangeRate(request.currency()) >> new CurrencyExchangeRateDTO(
                'success', request.currency(), ["GBP": 1.0, "USD": 1.2674, "EUR": 1.1707, "AFN": 93.2239]
        )
        
        and: 'check result message and balance'
        result.message() == 'Funds successfully transferred'

        def accountFromUpdated = clientAccountService.findClientAccount(fromAccount.id())
        accountFromUpdated.balance == 998.732600
        
        def accountToUpdated = clientAccountService.findClientAccount(toAccount.id())
        accountToUpdated.balance == 751.517400
        
        and: 'check transaction history'
        def transactions = clientTransactionService.listClientTransactions(fromAccount.id(), 0, 10, 'dateCreated')
        transactions.size() == 1
        transactions[0].targetClientAccountId() == toAccount.id()
        transactions[0].amountTransferred() == 1.0G
        transactions[0].status() == TransactionStatus.COMPLETED
        transactions[0].currency() == request.currency()
    }

    def 'test executeClientFundTransaction() - invalid target account currency should fail'() {
        given:
        def fromAccount = clientAccountService.listClientAccounts(UUID.fromString('e9e187d7-2ac3-4a71-bb59-5ca84c4a7d66'))[0] // USD account
        def toAccount = clientAccountService.listClientAccounts(UUID.fromString('b1fd0df8-82f2-48f5-91ea-93b7b1d7dab4'))[0]   // EUR account
        def request = new FundTransactionRequestDTO(fromAccount.id(), toAccount.id(), 1.0G, "GBP")
        
        when:
        clientTransactionService.executeClientFundTransaction(request)
        
        then: 'should no be any request to exchange service'
        0 * currencyExchangeRateService.getCurrencyExchangeRate(request.currency())
        
        and: 'invalid target currency'
        def e = thrown(ApiException)
        e.message == "Invalid target currency. Can't transfer [${request.currency()}] to [${toAccount.currency()}] account"
    }

    def 'test executeClientFundTransaction() - no enough funds to transfer should fail'() {
        given:
        def fromAccount = clientAccountService.listClientAccounts(UUID.fromString('b1fd0df8-82f2-48f5-91ea-93b7b1d7dab4'))[0] // EUR account
        def toAccount = clientAccountService.listClientAccounts(UUID.fromString('a7ff2c04-977e-455c-8d11-f6a2e7ca6259'))[0]   // EUR account
        def request = new FundTransactionRequestDTO(fromAccount.id(), toAccount.id(), 555.61G, "EUR")

        when: 'check before amount and try to execute transaction'
        fromAccount.balance() == 555.60
        toAccount.balance() == 1650.60

        clientTransactionService.executeClientFundTransaction(request)

        then: 'should no be any request for the same account currency, no need to convert money'
        0 * currencyExchangeRateService.getCurrencyExchangeRate(request.currency())

        and: 'insufficient funds error'
        def e = thrown(ApiException)
        e.message == "Not enough funds to proceed"
        
        and: 'check that balance is not changed'
        clientAccountService.findClientAccount(fromAccount.id()).balance == 555.60
        clientAccountService.findClientAccount(toAccount.id()).balance == 1650.60
    }
}

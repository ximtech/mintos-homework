package mintos.homework

import mintos.homework.exception.ApiException
import mintos.homework.service.ClientAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql(scripts = '/test-data.sql', executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class ClientAccountIntegrationSpec extends DatabaseSpecTemplate {

    @Autowired
    ClientAccountService clientAccountService

    def 'test listClientAccounts() - should return accounts by client id'() {
        when:
        def accounts = clientAccountService.listClientAccounts(UUID.fromString('1e084112-5715-4c43-aeae-ac6aa0518623'))
        
        then: 'should return accounts stored in db'
        accounts.size() == 2
        accounts[0].id() != null
        accounts[0].clientId() as String == '1e084112-5715-4c43-aeae-ac6aa0518623'
        accounts[0].currency() == 'USD'
        accounts[0].balance() == 1000.00G

        accounts[1].id() != null
        accounts[1].clientId() as String == '1e084112-5715-4c43-aeae-ac6aa0518623'
        accounts[1].currency() == 'USD'
        accounts[1].balance() == 800.50G
        
        when: 'single account'
        accounts = clientAccountService.listClientAccounts(UUID.fromString('6aa28db0-a9fe-499d-8c11-bef0d505ee94'))
        
        then:
        accounts.size() == 1
        accounts[0].id() != null
        accounts[0].clientId() as String == '6aa28db0-a9fe-499d-8c11-bef0d505ee94'
        accounts[0].currency() == 'AFN'
        accounts[0].balance() == 800.50G
        
        when: 'not existing account'
        accounts = clientAccountService.listClientAccounts(UUID.randomUUID())

        then:
        accounts.size() == 0
    }

    def 'test findClientAccount() - should find existing account by id or throw exception'() {
        given:
        def existingClientAccount = clientAccountService.listClientAccounts(UUID.fromString('6aa28db0-a9fe-499d-8c11-bef0d505ee94'))[0]
        
        when: 'account entity should be found by id'
        def account = clientAccountService.findClientAccount(existingClientAccount.id())
        
        then:
        notThrown(ApiException)
        account.id == existingClientAccount.id()
        account.createdByUser == 'Haven'
        account.updatedByUser == 'Daniels'
        account.dateCreated != null
        account.dateUpdated != null
        account.version != null
        !account.deleted
        
        account.clientId == existingClientAccount.clientId()
        account.currency == existingClientAccount.currency()
        account.balance == existingClientAccount.balance()
        
        when: 'not existing account should throw error'
        def notExistingId = UUID.randomUUID()
        clientAccountService.findClientAccount(notExistingId)
        
        then:
        def e = thrown(ApiException)
        e.message == "Account with id: [${notExistingId}] do not exist"
    }
}

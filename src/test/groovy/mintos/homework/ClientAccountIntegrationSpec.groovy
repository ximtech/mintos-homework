package mintos.homework

import mintos.homework.exception.ApiException
import mintos.homework.service.ClientAccountService
import org.springframework.beans.factory.annotation.Autowired

class ClientAccountIntegrationSpec extends DatabaseSpecTemplate {

    @Autowired
    ClientAccountService clientAccountService

    def 'test listClientAccounts() - should return accounts by client id'() {
        when:
        def accounts = clientAccountService.listClientAccounts(UUID.fromString('e9e187d7-2ac3-4a71-bb59-5ca84c4a7d66'))
        
        then: 'should return accounts stored in db'
        accounts.size() == 2
        accounts[0].id() != null
        accounts[0].clientId() as String == 'e9e187d7-2ac3-4a71-bb59-5ca84c4a7d66'
        accounts[0].currency() == 'USD'
        accounts[0].balance() == 1000.00G

        accounts[1].id() != null
        accounts[1].clientId() as String == 'e9e187d7-2ac3-4a71-bb59-5ca84c4a7d66'
        accounts[1].currency() == 'USD'
        accounts[1].balance() == 800.50G
        
        when: 'single account'
        accounts = clientAccountService.listClientAccounts(UUID.fromString('550e8400-e29b-41d4-a716-446655440000'))
        
        then:
        accounts.size() == 1
        accounts[0].id() != null
        accounts[0].clientId() as String == '550e8400-e29b-41d4-a716-446655440000'
        accounts[0].currency() == 'AFN'
        accounts[0].balance() == 800.50G
        
        when: 'not existing account'
        accounts = clientAccountService.listClientAccounts(UUID.randomUUID())

        then:
        accounts.size() == 0
    }

    def 'test findClientAccount() - should find existing account by id or throw exception'() {
        given:
        def existingClientAccount = clientAccountService.listClientAccounts(UUID.fromString('550e8400-e29b-41d4-a716-446655440000'))[0]
        
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

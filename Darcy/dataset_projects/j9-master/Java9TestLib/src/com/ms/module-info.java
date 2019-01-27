module com.ms.bankService{
    exports com.ms.service;
    provides com.ms.service.BankService
            with com.ms.serviceImpl.BankServiceImpl,
                    com.ms.serviceImpl.BankServiceImpl2;
}
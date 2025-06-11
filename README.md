# Chat4Me AuthService
Progetto di esercitazione sulla comunicazione tra API.

Il sistema si articola in tre servizi principali: l’API Gateway, il Servizio di Autenticazione e il Servizio di Messaggistica, ciascuno con un ruolo chiave.

---

## Servizio di autenticazione
Il Servizio di Autenticazione implementa un processo di login sicuro tramite Strong Customer Authentication (SCA).
L’autenticazione avviene in due fasi: prima con l’inserimento delle credenziali (username e password) e successivamente con la verifica di un codice OTP.
Una volta autenticato, l’utente riceve un token JWT utilizzabile per accedere agli altri servizi.
Inoltre, il servizio gestisce il rinnovo delle sessioni mediante il meccanismo di refresh token.

Il servizio espone le seguenti api:
- Login SCA* per la generazione di un token autorizzativo
- Verifica token autorizzativi
- Refresh token autorizzativi*

*Login SCA: Strong Customer Authentication, è un autenticazione forte a due fattori (password + OTP).
Si trova anche nello Swagger condiviso, viene prodotta una prima chiamata in cui si passa username e
password e una seconda per un OTP inviato tramite SMS (o altri canali come email o push). Non
si richiede l'implementazione un servizio di invio SMS, l’OTP deve essere creato randomicamente ma
verrà printato nella console così da poter svolgere delle prove.

*Refresh token autorizzativi: i token dovranno avere una scadenza, esiste una tecnologia denominata
OAuth2, utilizzarla come spunto per creare un token con refresh.

---

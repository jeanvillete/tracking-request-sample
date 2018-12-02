# tracking-request-sample
tracking-request-sample

This project show a Java Web Application tracking requests via a Filter service, furthermore, this show this behaviour with some spring mvc features, such as Controller, autowired, and so on.

[lang = pt_BR]
objetivo;
Colocar uma propriedade com um valor gerado randomicamente num objeto request, como uma propriedade deste objeto request e obter o valor desta propriedade num serviço com escopo de request num segundo momento.
    O valor gerado randomicamente deve ser de um serviço com escopo default (Singleton), e o único serviço que este expõe, é justamente a geração deste valor randomico (UUID).

A propriedade será introduzida no objeto request em um Filter, ou seja, antes de que a requisição chegue num Controller/Servlet que por sua vez seria o responsável por invocar o tal serviço.

Este serviço mencionado, é instanciado a cada requisição sob demanda e gerência do próprio Spring Framework, e deve estar anotado neste serviço uma propriedade autowired para HttpServletRequest, justamente para recuperar desta ultima a   tal propriedade injetada via o Filter.

[run command]
mvn jetty:run


# Projeto ADA lógica de programação I - Santander Coders 2023

O projeto consiste na criação de uma agenda de contatos, onde se possa registrar vários contatos e seus dados referentes a números telefônicos.

A aplicação deveria considerar como 
1) Requisitos não funcionais: Utilizar arquivos de texto para armazenar os dados (Simular base de dados);
2) Requisitos funcionais: Não é permitido armazenar contatos com o mesmo id; Não é permitido armazenar contatos com telefones ja cadastrados; Para realizar as ações, será necessário informar o id do contato.



## Funcionalidades

- Adicionar contato

Método para adicionar um novo contato à agenda: Verifica se os números de telefone já estão cadastrados em outros contatos, verifica se o ID do contato já está em uso cria uma lista de objetos Telefone com os números informados, cria um novo contato e adiciona à lista de contatos, incrementa o ID do próximo contato, Salva os contatos no arquivo;

- Remover contato;


- Editar contato
Edita um contato da agenda: busca pelo contato com o ID informado, Verifica se os novos números já estão cadastrados em outros contatos, atualiza os dados do contato com os novos valores informados e salva os contatos atualizados no arquivo;

## FAQ

#### Como o programa garante que não serão armazenados contatos com o mesmo ID:

No código, há uma parte que se certifica de que não são salvos contatos que tenham o mesmo número de identificação, chamado ID. Isso é importante para que cada pessoa tenha seu próprio número único na agenda.

Como isso funciona? O código verifica se já existe algum contato com o mesmo número de identificação que o novo contato que está sendo adicionado. Se isso acontecer, significa que dois contatos teriam o mesmo número de identificação, o que não é permitido. Nesse caso, o programa mostra uma mensagem de erro, evitando que o contato seja adicionado com o mesmo ID de outro.

#### Como o programa garante que não serão armazenados contatos com telefones já cadastrados:

No código, há uma parte que se certifica de que não são salvos contatos com números de telefone que já foram cadastrados para outros contatos. Isso é importante para evitar que dois contatos compartilhem o mesmo número.

Como isso funciona? O código verifica se algum contato já possui o número de telefone que está sendo adicionado. Se encontrar um contato com o mesmo número de telefone, significa que esse número já está sendo usado por outra pessoa na agenda. Nesse caso, o programa mostra uma mensagem de erro e não permite adicionar o novo contato com o mesmo número de telefone que já existe.


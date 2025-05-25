# Leitor de Texto Acessível

Um aplicativo Java que realiza a leitura em voz alta de arquivos de texto, desenvolvido como parte de uma atividade acadêmica sobre tecnologias inclusivas.

## 📋 Sobre o Projeto

Este projeto foi desenvolvido como atividade alternativa para a disciplina de Programação de Computadores no curso de ADS do IFSP-Caraguatatuba, no contexto do evento institucional em comemoração ao Dia Mundial da Diversidade Cultural para o Diálogo e o Desenvolvimento (21 de maio de 2025).

O tema central do evento foi "Conexões que Transformam: Diversidade Cultural e Inclusão Tecnológica" e inspirou a criação deste leitor de texto acessível, pensado como uma ferramenta para pessoas com deficiência visual.

## ✨ Funcionalidades

- Leitura automática em voz alta de qualquer arquivo de texto
- Navegação linha por linha controlada pelo usuário
- Três velocidades de leitura (Lenta, Normal, Rápida)
- Interface simples e acessível
- Controle intuitivo por atalhos de teclado
- Feedback visual do progresso da leitura

## 🔧 Requisitos

- Windows (utiliza o sintetizador de voz nativo do sistema)
- Java Runtime Environment (JRE) 8 ou superior
- Saída de áudio funcional

## 💻 Como usar

1. Execute o programa
2. Uma mensagem de boas-vindas será exibida e lida automaticamente
3. Selecione qualquer arquivo de texto para leitura
4. Escolha sua velocidade preferida (Lenta, Normal ou Rápida)
5. Use a barra de espaço para avançar e ouvir cada linha
6. Pressione ESC quando desejar encerrar a leitura

## ⌨️ Atalhos de teclado

- **Barra de espaço**: Lê a linha atual e avança para a próxima
- **ESC**: Encerra a leitura e fecha o programa

## 🔍 Detalhes técnicos

O aplicativo foi construído com:
- Java Swing para a interface gráfica
- System.Speech do Windows (via PowerShell) para síntese de voz
- Threads para permitir a leitura simultânea com a exibição dos diálogos

---

Desenvolvido por Vitor de Oliveira, maio/2025.

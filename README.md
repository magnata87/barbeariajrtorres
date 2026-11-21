# Barbearia Junior Torres — Android TV Launcher

Projeto inicial Android nativo para TV, com navegação por controle remoto e três telas: Início, Futebol ao Vivo e Pagamentos.

## O que já está implementado
- Launcher Android TV (categoria `LEANBACK_LAUNCHER`) e opção de tela HOME.
- Tela inicial com atalho **TV** que tenta abrir o pacote informado `com.integration.unitvsiptv`.
- Atalhos para YouTube, Netflix e Prime Video usando os pacotes conhecidos; os demais tiles aparecem, mas precisam dos package IDs corretos para abrir.
- Tela Futebol com estrutura preparada. **Ainda não há feed de partidas/canais conectado**; por isso não inventa jogos ou emissoras.
- Tela Pix com QR EMV estático gerado a partir da chave celular informada e botão de copiar chave.

## Limitações importantes antes de instalar
- O QR Pix foi montado com os dados fornecidos, mas deve ser testado no app do banco antes de divulgar/receber pagamentos. Confira o nome exibido e o destinatário antes de confirmar qualquer Pix.
- A integração com Spotify/controles de mídia ainda não foi implementada.
- Para preencher Futebol ao Vivo com dados reais e canais brasileiros, é necessário escolher/conectar um provedor de dados e direitos de programação; não há credenciais de API incluídas.
- O APK desta etapa é **debug**, não assinado para distribuição final. A TV pode exigir confirmação para instalar apps de fontes desconhecidas.

## Gerar APK no GitHub pelo iPhone
1. Crie um repositório vazio no GitHub.
2. Extraia este ZIP no app Arquivos do iPhone.
3. No repositório, use **Add file → Upload files** e envie o conteúdo da pasta (incluindo `.github`).
4. Faça **Commit changes** na branch `main`.
5. Abra a aba **Actions** do repositório e aguarde `Build Android TV Launcher APK` concluir.
6. Abra a execução concluída e baixe o artefato `BarbeariaJuniorTorresLauncher-debug`.
7. Extraia o APK e transfira para a Android TV para instalar.

## Requisitos de compilação
- JDK 17
- Gradle 8.9
- Android Gradle Plugin 8.7.3 / compile SDK 35

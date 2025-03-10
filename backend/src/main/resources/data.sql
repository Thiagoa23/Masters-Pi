-- Inserindo usuários
INSERT INTO users (cpf, email, password, enabled, nome, role) VALUES
('306.877.030-00', 'admin@gmail.com', '$2a$10$FnpMotGRLk1Pte1UPwgzFuDQFWiqAYhDQYk4BXe1LDYFVb2s5YtvO', 1, 'Thiago', 'ADMIN'),
('844.886.980-00', 'estoq@gmail.com', '$2a$10$FnpMotGRLk1Pte1UPwgzFuDQFWiqAYhDQYk4BXe1LDYFVb2s5YtvO', 1, 'Lucas', 'ESTOQUISTA');

-- Inserindo produtos
INSERT INTO produtos (ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor) VALUES
(1, 4.5, 'Novo valorant tactibear valorant punho jogo pe...', 76, '4ee2e5ca-8fec-4cc1-8e7c-698afd0fab5cf_pelucia.jpeg', 'Pelucia valorant tactibear valorant', 53.97),
(1, 5.0, 'VALORANT Bonecos De Pelúcia Wingman', 28, '59783e8b-d3e3-4713-9d53-1e81f54843d4_pelucia4.jpeg', 'Pelucia Wingman Gekko', 75.35),
(0, 4.0, 'Pelúcia Pinguim Tático Boneca Coelho para Cria...', 13, NULL, 'Jogo de pelucia agentes valorant', 326.60);

-- Inserindo imagens dos produtos (ajustado para usar os IDs gerados automaticamente)
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
((SELECT codigo FROM produtos WHERE nome = 'Pelucia valorant tactibear valorant'), '45fc3695-1319-4671-854d-bcd80f308431_pelucia0.jpeg'),
((SELECT codigo FROM produtos WHERE nome = 'Pelucia valorant tactibear valorant'), '4ee2e5ca-8fec-4cc1-8e7c-698afd0fab5cf_pelucia.jpeg'),
((SELECT codigo FROM produtos WHERE nome = 'Pelucia Wingman Gekko'), '59783e8b-d3e3-4713-9d53-1e81f54843d4_pelucia4.jpeg'),
((SELECT codigo FROM produtos WHERE nome = 'Pelucia Wingman Gekko'), 'd1023db4-b216-4248-a3f3-7b699649c238_pelucia2.jpeg'),
((SELECT codigo FROM produtos WHERE nome = 'Jogo de pelucia agentes valorant'), 'baa8dd99-a16f-49c2-ae48-215c86ddee1c_WhatsApp_Image_2025-03-10_at_18.58.32.jpeg'),
((SELECT codigo FROM produtos WHERE nome = 'Jogo de pelucia agentes valorant'), '2acc902c-0908-4a56-adcd-e48c3d6e057e_WhatsApp_Image_2025-03-10_at_18.57.32.jpeg'),
((SELECT codigo FROM produtos WHERE nome = 'Jogo de pelucia agentes valorant'), '169f6c8a-6435-4a3a-9175-976810836acb_WhatsApp_Image_2025-03-10_at_18.57.44.jpeg'),
((SELECT codigo FROM produtos WHERE nome = 'Jogo de pelucia agentes valorant'), 'afea7bdb-1bf2-4375-a504-2bfcf71691ab_WhatsApp_Image_2025-03-10_at_18.57.56.jpeg');

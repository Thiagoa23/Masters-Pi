-- Inserindo usuários
INSERT INTO users (cpf, email, password, enabled, nome, role) VALUES
('306.877.030-00', 'admin@gmail.com', '$2a$10$FnpMotGRLk1Pte1UPwgzFuDQFWiqAYhDQYk4BXe1LDYFVb2s5YtvO', 1, 'Thiago', 'ADMIN'),
('844.886.980-00', 'estoq@gmail.com', '$2a$10$FnpMotGRLk1Pte1UPwgzFuDQFWiqAYhDQYk4BXe1LDYFVb2s5YtvO', 1, 'Lucas', 'ESTOQUISTA');

-- Inserindo novo usuário cliente@gmail.com com senha “123” (bcrypt hash gerado)
INSERT INTO users (cpf, email, password, enabled, nome, role) VALUES
('123.456.789-09', 'cliente@gmail.com', '$2b$10$5ZfXXMo.moVH0lxRTBOm8.z2uHPC79H2csWURWLlAjWVDcSY7h7fu', 1, 'Cliente', 'CLIENTE');

-- Inserindo produtos
INSERT INTO produtos (ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor) VALUES
(1, 4.5, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec consequat commodo tellus id dictum. Etiam ut massa vitae tellus imperdiet viverra. Phasellus non nulla pellentesque, imperdiet urna eget, condimentum quam. Praesent ac imperdiet ante, non congue leo. Fusce et quam ut nibh volutpat vulputate ac eu risus. Phasellus lobortis urna ut felis pharetra mollis. In ut ex a nisl posuere semper. Nunc non eros sit amet nunc lobortis consequat sed hendrerit enim. Aliquam tincidunt libero ac sem auctor aliquet. Nullam magna ligula, ultricies non ornare sed, tempus nec tellus. Etiam.', 76, '4ee2e5ca-8fec-4cc1-8e7c-698afd0fab5cf_pelucia.jpeg', 'Pelucia valorant tactibear valorant', 53.97),
(1, 5.0, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque ac vehicula mauris, a hendrerit nisl. Integer varius ligula erat, et viverra nisi auctor eget. Fusce cursus est vitae nulla fermentum pharetra. Nulla sit amet vulputate tortor. Donec sed nunc vitae ligula congue venenatis at nec mi. Proin consectetur urna quis nisi tincidunt, ac placerat ex dictum. Vivamus lacinia sem leo, id posuere sem elementum vel. Integer semper tempus neque et.', 28, '59783e8b-d3e3-4713-9d53-1e81f54843d4_pelucia4.jpeg', 'Pelucia Wingman Gekko', 75.35),
(0, 4.0, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum et eros congue, gravida lectus consequat, vulputate tortor. Fusce non nisl lobortis, sagittis leo et, molestie arcu. Pellentesque vel eleifend arcu. Ut vulputate non ligula et cursus. Etiam gravida, velit ut dapibus molestie, ex enim sodales risus, eu feugiat purus metus.', 13, 'baa8dd99-a16f-49c2-ae48-215c86ddee1c_WhatsApp_Image_2025-03-10_at_18.58.32.jpeg', 'Jogo de pelucia agentes valorant', 326.60);

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

-- Inserindo usuários
INSERT INTO users (cpf, email, password, enabled, nome, role) VALUES
('306.877.030-00', 'admin@gmail.com', '$2a$10$FnpMotGRLk1Pte1UPwgzFuDQFWiqAYhDQYk4BXe1LDYFVb2s5YtvO', 1, 'Thiago', 'ADMIN'),
('844.886.980-00', 'estoq@gmail.com', '$2a$10$FnpMotGRLk1Pte1UPwgzFuDQFWiqAYhDQYk4BXe1LDYFVb2s5YtvO', 1, 'Lucas', 'ESTOQUISTA');

-- Inserindo novo usuário cliente@gmail.com com senha “123” (bcrypt hash gerado)
INSERT INTO users (cpf, email, password, enabled, nome, role) VALUES
('123.456.789-09', 'cliente@gmail.com', '$2b$10$5ZfXXMo.moVH0lxRTBOm8.z2uHPC79H2csWURWLlAjWVDcSY7h7fu', 1, 'Cliente', 'CLIENTE');

-- ------------------------------------------------------------
-- Inserindo 10 novos produtos (com UUID + nome de arquivo)
-- ------------------------------------------------------------

-- 1) Whey Protein 80% Concentrado – Sabor Vanilla (900 g)
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.8,
    'Whey Protein concentrado 80% sabor Vanilla: fonte de proteína de alto valor biológico, auxilia no ganho e manutenção de massa muscular e recuperação pós-treino. Embalagem de 900 g, ideal para quem busca sabor suave de baunilha e máximo rendimento.',
    100,
    '50521c70-15b9-4b1c-9b0d-30e702a170f2.png',  -- lifestyle (cenário)
    'Whey Protein 80% Concentrado Sabor Vanilla',
    129.90
);

-- 2) Whey Protein 80% Concentrado – Sabor Coffee (900 g)
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.7,
    'Whey Protein concentrado 80% sabor Coffee: suplemento proteico com intenso sabor de café, perfeito para quem busca maior energia e recuperação muscular. Embalagem de 900 g, excelente rendimento e sabor marcante.',
    100,
    '5ab46ea6-f28c-4380-9599-235428e5baba.png',  -- lifestyle (cenário)
    'Whey Protein 80% Concentrado Sabor Coffee',
    129.90
);

-- 3) Whey Protein 80% Concentrado – Sabor Dulce de Leche (900 g)
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.9,
    'Whey Protein concentrado 80% sabor Dulce de Leche: cremosidade e doçura equilibradas, excelente para quem busca um suplemento proteico saboroso e de alta qualidade. Embalagem de 900 g para rendimento prolongado.',
    100,
    '96b2d217-4670-48c9-a74c-63fdf2ac4ae8.png',  -- lifestyle (cenário)
    'Whey Protein 80% Concentrado Sabor Dulce de Leche',
    129.90
);

-- 4) Whey Protein 80% Concentrado – Sabor Cookies (900 g)
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.8,
    'Whey Protein concentrado 80% sabor Cookies: suplemento proteico com pedaços de cookie para sabor irresistível. Embalagem de 900 g, ideal para quem busca massa muscular com sabor diferenciado.',
    100,
    'c0a80d09-757e-4bdc-be2e-968203103341.png',  -- lifestyle (cenário)
    'Whey Protein 80% Concentrado Sabor Cookies',
    129.90
);

-- 5) Whey Protein 80% Concentrado – Sabor Chocolate (900 g)
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.8,
    'Whey Protein Concentrado 80% sabor Chocolate: suplemento em pó com alto teor de proteína, 24 g por porção, 5.3 g de BCAA e zero glúten por dose. Embalagem de 900 g, acabamento premium e sabor intenso de chocolate.',
    100,
    '59ed3362-3b96-43f6-bd06-477a7c5fba32.png',  -- lifestyle (cenário)
    'Whey Protein 80% Concentrado Sabor Chocolate – 900 g',
    129.90
);

-- 6) Whey Protein 80% Concentrado – Sabor Morango (900 g)
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.8,
    'Whey Protein Concentrado 80% sabor Morango: suplemento em pó com 24 g de proteína, 5.3 g de BCAA e zero glúten, sabor frutado de morango natural. Embalagem de 900 g para rendimento prolongado e nutrição de alta qualidade.',
    100,
    '982a15b7-33b9-46cc-9985-d5a0100bb668.png',  -- lifestyle (cenário)
    'Whey Protein 80% Concentrado Sabor Morango – 900 g',
    129.90
);

-- 7) Whey Proteínas Sabor Chocolate – 15 g (250 ml)
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.3,
    'Whey Proteínas sabor Chocolate: bebida láctea com 15 g de proteína do soro do leite por unidade de 250 ml, zero lactose, 2.9 g de BCAA por porção. Ideal para intolerantes à lactose e recuperação muscular rápida.',
    200,
    'd1e2f3a4-b5c6-47d8-91e0-f2a3b4c5d6e7_whey_proteinas_sabor_chocolate_15g_250ml.png',  -- frontal (fundo branco)
    'Whey Proteínas Sabor Chocolate – 15 g (250 ml)',
    9.90
);

-- 8) Whey Proteínas Sabor Morango – 15 g (250 ml)
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.3,
    'Whey Proteínas sabor Morango: bebida láctea esterilizada com 15 g de proteína do soro, sabor morango natural, 2.9 g de BCAA por porção, zero lactose. Ideal para recuperação pós-treino com toque frutado.',
    200,
    'e1f2a3b4-e5d6-47e8-91f0-2a3b4c5d6e7f_whey_proteinas_sabor_morango_15g_250ml.png',  -- frontal (fundo branco)
    'Whey Proteínas Sabor Morango – 15 g (250 ml)',
    9.90
);

-- 9) Creatina Monohidratada 100% Pura – Pote Verde (250 g)
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.6,
    'Creatina Monohidratada 100% pura, importada da Alemanha, embalagem de 250 g. Auxilia no aumento de força, explosão muscular e recuperação entre séries. Selo “Made in Germany” garante qualidade.',
    200,
    'a7b2d9f1-c34e-4f2a-9d1b-f6e3a8c9d2b0_creatina_monohidratada_pote_verde_250g.png',  -- frontal (fundo branco)
    'Creatina Monohidratada 100% Pura – Pote Verde 250g',
    79.90
);

-- 10) Creatina Monohidratada 100% Pura – Pote Branco (250 g)
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.6,
    'Creatina Monohidratada 100% pura em pote branco, embalagem de 250 g. Ação rápida para maior explosão muscular e auxílio na força em treinos de alta intensidade. Fórmula importada para máxima pureza.',
    200,
    '5c9d8a4b-72f3-4e6a-9a1b-e8c7f9b2d3a4_creatina_monohidratada_pote_branco_250g.png',  -- frontal (fundo branco)
    'Creatina Monohidratada 100% Pura – Pote Branco 250g',
    79.90
);

-- 11) Multivitamínico em Cápsulas – 120 Cápsulas
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.5,
    'Multivitamínico em cápsulas, nova fórmula com mais de 20 vitaminas e minerais essenciais. Embalagem de 120 cápsulas, ideal para suplementação diária. Ajuda na imunidade, disposição e saúde geral.',
    150,
    '2f7e1c3b-d91a-4a5b-8c2f-e7d6a9b4c5d2_multi_suplemento_120_capsulas.png',  -- frontal (fundo branco)
    'Multivitamínico em Cápsulas – 120 Cápsulas',
    49.90
);

-- 12) Multivitamínico em Cápsulas – 60 Cápsulas
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.5,
    'Multivitamínico em cápsulas, nova fórmula com vitaminas e minerais completos. Embalagem de 60 cápsulas, prática para quem deseja teste inicial ou dose reduzida. Ajuda na energia, imunidade e bem-estar.',
    150,
    'c6d4a2f7-eb39-4b1e-8382-f9a7d3e6b2c1_multi_suplemento_60_capsulas.png',  -- frontal (fundo branco)
    'Multivitamínico em Cápsulas – 60 Cápsulas',
    29.90
);

-- 13) Óleo de Peixe Ultra – 75 Cápsulas
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.4,
    'Óleo de Peixe Ultra: ômega-3 concentrado em 75 cápsulas gelatinosas. Auxilia na saúde cardiovascular, articular e cerebral. Alta pureza, livre de metais pesados, fórmula avançada para suporte diário.',
    120,
    '84b3d9c2-a471-4e86-b3f1-d92a6b8f4e5c_oleo_de_peixe_ultra_75_capsulas_preto.png',  -- frontal (fundo branco)
    'Óleo de Peixe Ultra – 75 Cápsulas',
    39.90
);

-- 14) Vitamina D Ultra – 120 Cápsulas
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.7,
    'Vitamina D Ultra: nova fórmula em cápsulas com alta concentração de vitamina D3, auxilia na absorção de cálcio, saúde óssea e imunidade. Embalagem com 120 cápsulas para suprir a dose diária por 4 meses.',
    100,
    'f1a2b3c4-d5e6-4f7a-8901-2b3c4d5e6f7a_vitamina_d_ultra_120_capsulas.png',  -- frontal (fundo branco)
    'Vitamina D Ultra – 120 Cápsulas',
    59.90
);

-- 15) Vitamina B12 – 120 Cápsulas
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.6,
    'Vitamina B12: suplemento alimentar em cápsulas com 120 unidades de cianocobalamina, auxilia na produção de energia, função neurológica e formação de glóbulos vermelhos. Fórmula de alta absorção.',
    100,
    'a1b2c3d4-e5f6-47a8-910b-2c3d4e5f6a7b_vitamina_b12_120_capsulas.png',  -- frontal (fundo branco)
    'Vitamina B12 – 120 Cápsulas',
    49.90
);

-- 16) Vitamina C 1000 mg – 120 Comprimidos
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.8,
    'Vitamina C 1000 mg: suplemento em comprimidos com 120 doses diárias; antioxidante poderoso que auxilia na imunidade, saúde da pele e combate ao estresse oxidativo para maior disposição.',
    100,
    'b1c2d3e4-f5a6-48b9-012c-3d4e5f6a7b8c_vitamina_c_1000mg_120_comprimidos.png',  -- frontal (fundo branco)
    'Vitamina C 1000 mg – 120 Comprimidos',
    39.90
);

-- 17) Pasta de Amendoim Natural – 1,005 kg
INSERT INTO produtos (
    ativo, avaliacao, descricao, estoque, imagem_principal, nome, valor
) VALUES (
    1,
    4.5,
    'Pasta de Amendoim Natural: 100% amendoim puro, sem adição de óleo ou açúcar, embalagem de 1,005 kg. Fonte de proteínas vegetais, gorduras saudáveis e fibras para energia e saciedade prolongada.',
    80,
    'c1d2e3f4-0a1b-42c3-45d6-7e8f901a2b3c_pasta_de_amendoim_natural_1005kg.png',  -- frontal (fundo branco)
    'Pasta de Amendoim Natural – 1,005 kg',
    29.90
);

-- ############################################################
-- 1º BLOCO: SKUs com 3 imagens (lifestyle = imagem_principal)
-- ############################################################

-- 1) Whey Protein 80% Concentrado – Sabor Vanilla (900 g)
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Vanilla'),
  'd94f1eac-bb23-4e29-81d3-a9f4b1e491c2_whey_protein_80_concentrado_vanilla.png'      -- frontal (fundo branco)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Vanilla'),
  '31137dfb-30b1-4762-a3b9-ab298dfdb3af.png'                                           -- verso (informação nutricional)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Vanilla'),
  '50521c70-15b9-4b1c-9b0d-30e702a170f2.png'                                           -- lifestyle (cenário; já usado em imagem_principal)
);

-- 2) Whey Protein 80% Concentrado – Sabor Coffee (900 g)
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Coffee'),
  'b3a762e2-e947-4c5f-9d26-7f8c2b8e2a77_whey_protein_80_concentrado_coffee.png'         -- frontal (fundo branco)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Coffee'),
  'ec1c0079-1efe-461b-8aa7-8d04f68b806d.png'                                           -- verso (informação nutricional)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Coffee'),
  '5ab46ea6-f28c-4380-9599-235428e5baba.png'                                           -- lifestyle (cenário; já usado em imagem_principal)
);

-- 3) Whey Protein 80% Concentrado – Sabor Dulce de Leche (900 g)
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Dulce de Leche'),
  'e187c4ad-a536-4e1b-8f3d-2b4a7c6e9f0d_whey_protein_80_concentrado_dulce_de_leche.png'  -- frontal (fundo branco)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Dulce de Leche'),
  '144a8790-9e48-4c36-85b1-906a71f80ff5.png'                                           -- verso (informação nutricional)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Dulce de Leche'),
  '96b2d217-4670-48c9-a74c-63fdf2ac4ae8.png'                                           -- lifestyle (cenário; já usado em imagem_principal)
);

-- 4) Whey Protein 80% Concentrado – Sabor Cookies (900 g)
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Cookies'),
  'f7a3d9b1-e428-4c6d-9b5f-ad2e6c8f1d3b_whey_protein_80_concentrado_cookies.png'        -- frontal (fundo branco)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Cookies'),
  'ChatGPT Image 31 de mai. de 2025, 22_00_32.png'                                       -- verso (informação nutricional)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Cookies'),
  'c0a80d09-757e-4bdc-be2e-968203103341.png'                                           -- lifestyle (cenário; já usado em imagem_principal)
);

-- 5) Whey Protein 80% Concentrado – Sabor Chocolate (900 g)
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Chocolate – 900 g'),
  'f1e2d3c4-b5a6-47c8-91e0-2f3a4b5c6d7e_whey_protein_80_concentrado_sabor_chocolate_900g.png'  -- frontal (fundo branco)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Chocolate – 900 g'),
  '489682aa-2002-4c95-b15f-b3959731a209.png'                                           -- verso (informação nutricional)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Chocolate – 900 g'),
  '59ed3362-3b96-43f6-bd06-477a7c5fba32.png'                                           -- lifestyle (cenário; já usado em imagem_principal)
);

-- 6) Whey Protein 80% Concentrado – Sabor Morango (900 g)
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Morango – 900 g'),
  'a1b2c3d4-e5f6-47c8-91e0-2f3a4b5c6d7f_whey_protein_80_concentrado_sabor_morango_900g.png'  -- frontal (fundo branco)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Morango – 900 g'),
  '9b96c363-4911-49a0-b418-3adaeadea6b7.png'                                           -- verso (informação nutricional)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Protein 80% Concentrado Sabor Morango – 900 g'),
  '982a15b7-33b9-46cc-9985-d5a0100bb668.png'                                           -- lifestyle (cenário; já usado em imagem_principal)
);

-- ############################################################
-- 2º BLOCO: SKUs com 2 imagens (frontal + verso)
-- ############################################################

-- 7) Whey Proteínas Sabor Chocolate – 15 g (250 ml)
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Proteínas Sabor Chocolate – 15 g (250 ml)'),
  'd1e2f3a4-b5c6-47d8-91e0-f2a3b4c5d6e7_whey_proteinas_sabor_chocolate_15g_250ml.png'       -- frontal (fundo branco)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Proteínas Sabor Chocolate – 15 g (250 ml)'),
  'f10a9f36-3ae8-4ad4-8646-eef0e61edc15.png'                                              -- verso (informação nutricional)
);

-- 8) Whey Proteínas Sabor Morango – 15 g (250 ml)
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Proteínas Sabor Morango – 15 g (250 ml)'),
  'e1f2a3b4-e5d6-47e8-91f0-2a3b4c5d6e7f_whey_proteinas_sabor_morango_15g_250ml.png'         -- frontal (fundo branco)
),
(
  (SELECT codigo FROM produtos WHERE nome = 'Whey Proteínas Sabor Morango – 15 g (250 ml)'),
  '974c754f-1e06-479d-8b70-b258be71a03a.png'                                              -- verso (informação nutricional)
);

-- ############################################################
-- 3º BLOCO: SKUs com apenas 1 imagem (frontal)
-- ############################################################

-- 9) Creatina Monohidratada 100% Pura – Pote Verde 250g
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Creatina Monohidratada 100% Pura – Pote Verde 250g'),
  'a7b2d9f1-c34e-4f2a-9d1b-f6e3a8c9d2b0_creatina_monohidratada_pote_verde_250g.png'  -- frontal (fundo branco)
);

-- 10) Creatina Monohidratada 100% Pura – Pote Branco 250g
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Creatina Monohidratada 100% Pura – Pote Branco 250g'),
  '5c9d8a4b-72f3-4e6a-9a1b-e8c7f9b2d3a4_creatina_monohidratada_pote_branco_250g.png'  -- frontal (fundo branco)
);

-- 11) Multivitamínico em Cápsulas – 120 Cápsulas
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Multivitamínico em Cápsulas – 120 Cápsulas'),
  '2f7e1c3b-d91a-4a5b-8c2f-e7d6a9b4c5d2_multi_suplemento_120_capsulas.png'  -- frontal (fundo branco)
);

-- 12) Multivitamínico em Cápsulas – 60 Cápsulas
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Multivitamínico em Cápsulas – 60 Cápsulas'),
  'c6d4a2f7-eb39-4b1e-8382-f9a7d3e6b2c1_multi_suplemento_60_capsulas.png'  -- frontal (fundo branco)
);

-- 13) Óleo de Peixe Ultra – 75 Cápsulas (apesar de ter 2 imagens, este SKU já foi incluído acima no item “9” => está como frontal + verso; portanto, *não* repetir)
--     Se por algum motivo você quiser repetir ou faturar para uma única imagem, basta manter somente a frontal:
-- INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
-- (
--   (SELECT codigo FROM produtos WHERE nome = 'Óleo de Peixe Ultra – 75 Cápsulas'),
--   '84b3d9c2-a471-4e86-b3f1-d92a6b8f4e5c_oleo_de_peixe_ultra_75_capsulas_preto.png'
-- );
-- INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
-- (
--   (SELECT codigo FROM produtos WHERE nome = 'Óleo de Peixe Ultra – 75 Cápsulas'),
--   'd5e2f4a8-c6b3-4f1d-a7e2-8b3f9c7d6a4e_oleo_de_peixe_ultra_75_capsulas_verde.png'
-- );

-- 14) Vitamina D Ultra – 120 Cápsulas
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Vitamina D Ultra – 120 Cápsulas'),
  'f1a2b3c4-d5e6-4f7a-8901-2b3c4d5e6f7a_vitamina_d_ultra_120_capsulas.png'  -- frontal (fundo branco)
);

-- 15) Vitamina B12 – 120 Cápsulas
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Vitamina B12 – 120 Cápsulas'),
  'a1b2c3d4-e5f6-47a8-910b-2c3d4e5f6a7b_vitamina_b12_120_capsulas.png'  -- frontal (fundo branco)
);

-- 16) Vitamina C 1000 mg – 120 Comprimidos
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Vitamina C 1000 mg – 120 Comprimidos'),
  'b1c2d3e4-f5a6-48b9-012c-3d4e5f6a7b8c_vitamina_c_1000mg_120_comprimidos.png'  -- frontal (fundo branco)
);

-- 17) Pasta de Amendoim Natural – 1,005 kg
INSERT INTO produto_imagens (produto_id, caminho_imagem) VALUES
(
  (SELECT codigo FROM produtos WHERE nome = 'Pasta de Amendoim Natural – 1,005 kg'),
  'c1d2e3f4-0a1b-42c3-45d6-7e8f901a2b3c_pasta_de_amendoim_natural_1005kg.png'  -- frontal (fundo branco)
);



INSERT INTO plano (nome, preco, limite_tipos_ia, permite_foto, permite_arquivo, limite_arquivos_mensagem, permite_audio) VALUES
('Free', 0.00, 1, false, false, 0, false),
('Básico', 19.90, 3, false, false, 0, false),
('Medium', 49.90, 2, true, true, 1, false),
('Pro', 89.90, 0, true, true, 0, true)
ON CONFLICT DO NOTHING;

INSERT INTO tipo_ia_aia (nome, descricao, icone, prompt_sistema, modelo_ia) VALUES
('Assistente pessoal', 'Ajuda com tarefas do dia a dia', '🧠', 'Você é um assistente pessoal prestativo e direto.', 'llama-3.3-70b-versatile'),
('Assistente de vendas', 'Ajuda a fechar negócios e responder clientes', '💼', 'Você é um especialista em vendas consultivas.', 'llama-3.3-70b-versatile')
ON CONFLICT DO NOTHING;

CREATE TABLE public.vector_store (
    id uuid PRIMARY KEY,
    content text,
    metadata jsonb,
    embedding vector(768)
);
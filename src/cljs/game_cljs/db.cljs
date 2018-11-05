(ns game-cljs.db)

(def direcoes
  {:esquerda [-1 0]
   :direita [1 0]
   :baixo [0 1]
   :cima [0 -1]})

(defn cria-snake []
  {
   :corpo [[5 3] [4 3] [3 3] [2 3]]
   :direcao :direita
   :cor "green"
   })

(defn move [{:keys [corpo direcao] :as snake} & comeu-maca]
  (assoc snake :corpo
         (let [[x-dir y-dir] (direcao direcoes)
               [x-head y-head] (first corpo)]
           (cons [(+ x-dir x-head) (+ y-dir y-head)] 
                 (if comeu-maca corpo (butlast corpo))))))

(defn vira [snake direcao] 
  (assoc snake :direcao direcao))

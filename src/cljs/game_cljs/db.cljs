(ns game-cljs.db)

(defn cria-snake []
  {
   :corpo [[3 0] [2 0] [1 0] [0 0]]
   :direcao [1 0]
   :cor "green"
   })

(defn move [{:keys [corpo direcao] :as snake}]
  (assoc snake :corpo
         (let [[x-dir y-dir] direcao
               [x-head y-head] (first corpo)]
           (cons [(+ x-dir x-head) (+ y-dir y-head)] (butlast corpo))))
)

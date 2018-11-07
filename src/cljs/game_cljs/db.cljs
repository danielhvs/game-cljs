(ns game-cljs.db
  (:require [re-frame.core :as rf])) 

(def w-max 20)
(def h-max 20)
(def tam-ganhar 8)

(def direcoes
  {
   :esquerda [-1 0]
   :esquerda-cima [-1 -1]
   :esquerda-baixo [-1 1]
   :direita-cima [1 -1]
   :direita-baixo [1 1]
   :direita [1 0]
   :baixo [0 1]
   :cima [0 -1]})

(defn cria-snake []
  {
   :corpo [[6 3] [5 3] [4 3] [3 3] [2 3]]
   :direcao :direita
   :cor "green"
   })
(defn cria-maca []
  {
   :local [(rand-int w-max) (rand-int h-max)]
   :cor "red"
   }
)

(defn corpo-bateu-em-si [{:keys [corpo] :as snake}]
  (let [head (first corpo)]
    (contains? (set (rest corpo)) head)))

(defn corpo-fora-limites [[head-x head-y]]
  (or
   (>= head-x w-max)
   (< head-x 0)
   (>= head-y h-max)
   (< head-y 0)
))

(defn perdeu? [snake]
  (or (corpo-bateu-em-si snake)
      (corpo-fora-limites (first (:corpo snake)))))

(defn ganhou? [{:keys [corpo]}]
  (>= (count corpo) tam-ganhar))

(defn move [{:keys [corpo direcao] :as snake} & comeu-maca]
  (assoc snake :corpo
         (let [[x-dir y-dir] (direcao direcoes)
               [x-head y-head] (first corpo)]
           (cons [(+ x-dir x-head) (+ y-dir y-head)] 
                 (if comeu-maca 
                   corpo
                   (butlast corpo))))))

(defn vira [snake direcao] 
  (assoc snake :direcao direcao))

(defn comendo-maca? [{[head] :corpo} {maca :local}]
  (= head maca))

(defn proximo-estado [snake maca]
  (if (ganhou? snake)
    (do
      (rf/dispatch [:ganhou])
      snake)
    (if (perdeu? snake) 
      (do
        (rf/dispatch [:perdeu])
        snake)
      (if (comendo-maca? snake maca)
        (move snake :grow)
        (move snake)))))

(defn proximo-estado-maca [snake maca]
  (if (comendo-maca? snake maca) 
    (cria-maca)
    maca))

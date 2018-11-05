(ns game-cljs.views
  (:require
   [re-frame.core :as re-frame]
   [game-cljs.subs :as subs]
   ))

(declare ->retangulo)

;; Funcoes
(defn cria-snake []
  {:posicao [[3 0] [2 0] [1 0] [0 0]]})

(defn desenha [xy cor]
  (->retangulo (first xy) (last xy) cor))

(defn desenha-snake [xy]
  (desenha xy "green"))

(defn desenha-maca [xy]
  (desenha xy "red"))

;; View
(def TAM_RET 15)

(defn retangulo [x y cor]
  [:rect
   {:width TAM_RET
    :height TAM_RET
    :fill cor
    :x x
    :y y}])

(defn ->retangulo [x y cor]
  (retangulo (* x TAM_RET) (* y TAM_RET) cor))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Hello from " @name]] 
    [:div
     [:svg 
      (let [w 800 h 600]
        {:view-box (str "0 0 " w " " h)
         :width w 
         :height h})
      (for [x (range 50)]
        (for [y (range 50)]
          (->retangulo x y "skyblue")))
      (map desenha-snake (:posicao (cria-snake)))
      (desenha-maca [10 23])
      ]
     ]))

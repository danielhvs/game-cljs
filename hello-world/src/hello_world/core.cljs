(ns hello-world.core
  (:require [play-cljs.core :as p]
            [goog.events :as events])
  (:require-macros [hello-world.music :refer [build-for-cljs]]))

(def w-max 40)
(def h-max 40)
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
      #_(rf/dispatch [:ganhou])
      snake)
    (if (perdeu? snake) 
      (do
        #_(rf/dispatch [:perdeu])
        snake)
      (if (comendo-maca? snake maca)
        (move snake :grow)
        (move snake)))))

(defn proximo-estado-maca [snake maca]
  (if (comendo-maca? snake maca) 
    (cria-maca)
    maca))

(declare ->retangulo)
(declare move )

;; Constantes
(def tam-ret 15)

;; Funcoes

;; View
(defn desenha [xy cor]
  (->retangulo (first xy) (last xy) cor))

(defn desenha-snake [{:keys [corpo cor] :as snake}]
  (map #(desenha % cor) corpo))

(defn desenha-maca [{:keys [local cor] :as maca}]
  (desenha local cor))

(defn retangulo [x y cor]
  [:rect
   {:width tam-ret
    :height tam-ret
    :fill cor
    :x x
    :y y}])

(defn ->retangulo [x y cor]
  (retangulo (* x tam-ret) (* y tam-ret) cor))

(defn desenha-mundo []
  (for [x (range w-max)]
        (for [y (range h-max)]
          (->retangulo x y "skyblue"))))

(defonce game (p/create-game (.-innerWidth js/window) (.-innerHeight js/window)))
(defonce state (atom {}))

(def main-screen
  (reify p/Screen
    (on-show [this]
      (reset! state {:text-x 20 :text-y 30 :snake (cria-snake) :maca (cria-maca)}))
    (on-hide [this])
    (on-render [this]
      (p/render game
                [[:fill {:color "skyblue"}
                  (desenha-mundo)]
                 (let [snake (:snake @state)]
                   [:fill {:color (:cor snake)}
                    (desenha-snake snake)])
                 (let [maca (:maca @state)]
                   [:fill {:color (:cor maca)}
                    (desenha-maca maca)])])
      (when
          (contains? (p/get-pressed-keys game) 37)
        (swap! state assoc-in [:snake :direcao] :esquerda))
      (when
          (contains? (p/get-pressed-keys game) 40)
        (swap! state assoc-in [:snake :direcao] :baixo))
      (when
          (contains? (p/get-pressed-keys game) 38)
        (swap! state assoc-in [:snake :direcao] :cima))
      (when
          (contains? (p/get-pressed-keys game) 39)
        (swap! state assoc-in [:snake :direcao] :direita))
      (swap! state assoc :snake (proximo-estado (:snake @state) (:maca @state)))
      (swap! state assoc :maca (proximo-estado-maca (:snake @state) (:maca @state)))

)))

(events/listen js/window "mousemove"
  (fn [event]
    (swap! state assoc :text-x (.-clientX event) :text-y (.-clientY event))))

(events/listen js/window "resize"
  (fn [event]
    (p/set-size game js/window.innerWidth js/window.innerHeight)))

(doto game
  (p/start)
  (p/set-screen main-screen))

; uncomment to generate a song and play it!

; (defonce audio (js/document.createElement "audio"))
; (set! (.-src audio) (build-for-cljs))
; (.play audio)

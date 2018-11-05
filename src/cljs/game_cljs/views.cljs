(ns game-cljs.views
  (:require
   [re-frame.core :as rf]
   [game-cljs.subs :as subs]
   ))

(declare ->retangulo)
(declare move )

;; Constantes
(def tam-ret 15)
(def w-max 10)
(def h-max 10)

;; Funcoes
(defn dispatch-timer-event
  []
  (let [now (js/Date.)]
    (rf/dispatch [:timer now])))
(defonce do-timer (js/setInterval dispatch-timer-event 500))



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

(defn main-panel []
  (let [
        snake (rf/subscribe [:snake])
        maca (rf/subscribe [:maca])
        ]
    [:div
     [:div [:button "->"]]
     [:div [:button "<-"]]
     [:div [:center
            [:svg 
             (let [w 800 h 600]
               {
                :view-box (str "0 0 " w " " h)
                :width w 
                :height h})
             (desenha-mundo)
             (desenha-maca @maca)
             (desenha-snake @snake)
             ]]]]
    ))

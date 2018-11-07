(ns game-cljs.views
  (:require
   [re-frame.core :as rf]
   [game-cljs.subs :as subs]
   [game-cljs.db :as db]
   [game-cljs.events :as events]
   ))

(declare ->retangulo)
(declare move )

;; Constantes
(def tam-ret 15)

;; Funcoes
(defn dispatch-timer-event
  []
  (let [now (js/Date.)]
    (rf/dispatch [:timer now])))
(defonce do-timer (js/setInterval dispatch-timer-event 200))

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
  (for [x (range db/w-max)]
        (for [y (range db/h-max)]
          (->retangulo x y "skyblue"))))

(defn main-panel []
  (let [
        snake (rf/subscribe [:snake])
        maca (rf/subscribe [:maca])
        feedback (rf/subscribe [:feedback])
        ]
    [:div
     [:div (str @feedback)]
     [:table [:tbody 
              [:tr
               [:td [:button {:on-click #(rf/dispatch [:muda-direcao :esquerda-cima])} "<^"]]
               [:td [:button {:on-click #(rf/dispatch [:muda-direcao :cima])} "^"]]
               [:td [:button {:on-click #(rf/dispatch [:muda-direcao :direita-cima])} "^>"]]
               ] 
              [:tr
               [:td [:button {:on-click #(rf/dispatch [:muda-direcao :esquerda])} "<"]]
               [:td [:button {:on-click #(rf/dispatch [::events/initialize-db])} "+"]]
               [:td [:button {:on-click #(rf/dispatch [:muda-direcao :direita])} ">"]]
               ]
              [:tr 
               [:td [:button {:on-click #(rf/dispatch [:muda-direcao :esquerda-baixo])} "<u"]]
               [:td [:button {:on-click #(rf/dispatch [:muda-direcao :baixo])} "u"]]
               [:td [:button {:on-click #(rf/dispatch [:muda-direcao :direita-baixo])} "u>"]]
               ]
              ]]

     [:div
      [:svg 
       (let [w 800 h 600]
         {
          :view-box (str "0 0 " w " " h)
          :width w 
          :height h})
       (desenha-mundo)
       (desenha-maca @maca)
       (desenha-snake @snake)
       ]]]
))

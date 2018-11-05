(ns game-cljs.views
  (:require
   [re-frame.core :as rf]
   [game-cljs.subs :as subs]
   ))

(declare ->retangulo)

;; Constantes
(def tam-ret 15)
(def w-max 50)
(def h-max 50)

;; Funcoes
(defn dispatch-timer-event
  []
  (let [now (js/Date.)]
    (rf/dispatch [:timer now])))  ;; <-- dispatch used

(defonce do-timer (js/setInterval dispatch-timer-event 1000))

(rf/reg-event-db                 ;; usage:  (dispatch [:timer a-js-Date])
  :timer                         ;; every second an event of this kind will be dispatched
  (fn [db [_ new-time]]          ;; note how the 2nd parameter is destructured to obtain the data value
    (assoc db :time new-time)))  ;; compute and return the new application state

(rf/reg-sub
  :time
  (fn [db _]     ;; db is current app state. 2nd unused param is query vector
    (:time db))) ;; return a query computation over the application state


(defn cria-snake []
  {
   :corpo [[3 0] [2 0] [1 0] [0 0]]
   :direcao [1 0]
   :cor "green"
   })

(defn cria-maca []
  {:local [(rand-int w-max) (rand-int h-max)]
   :cor "red"
   }
)

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

(defn main-panel []
  (let [name (rf/subscribe [::subs/name])
        timer @(rf/subscribe [:time])]
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
      (desenha-snake (cria-snake))
      (desenha-maca (cria-maca))
      ]
     ]))

(ns game-cljs.events
  (:require
   [re-frame.core :as rf]
   [game-cljs.db :as db]
   ))

;; Constantes
(def tam-ret 15)

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   {:snake (db/cria-snake)
    :maca (db/cria-maca)
    :feedback "Jogando..."}))

(rf/reg-event-db 
  :timer                  
  (fn [db [_ new-time]]   
    (assoc db 
      :snake (db/proximo-estado (:snake db) (:maca db))
      :maca (db/proximo-estado-maca (:snake db) (:maca db))
      (let [ctx (.getContext (.getElementById js/document "meu-canvas") "2d")
            snake (:snake db)
            maca (:maca db)]
        (set! (.-fillStyle ctx) "rgb(0,255,255)")
        (.clearRect ctx 0 0 320 320)
        (.beginPath ctx)
        (.rect ctx 0 0 300 300)
        (.fill ctx) 
        (doall
         (map (fn [[x y]] 
                (set! (.-fillStyle ctx) "rgb(0,0,255)")
                (.beginPath ctx)
                (.rect ctx (* tam-ret x) (* tam-ret y) tam-ret tam-ret)
                (.fill ctx)) 
              (:corpo snake)))
        (let [[x y] (:local maca)]
          (set! (.-fillStyle ctx) "rgb(255,0,0)")
          (.beginPath ctx)
          (.rect ctx (* tam-ret x) (* tam-ret y) tam-ret tam-ret)
          (.fill ctx)))
      
)))

(rf/reg-event-db 
  :muda-direcao
  (fn [db [_ nova-direcao]]   
    (assoc db 
      :snake (db/vira (:snake db) nova-direcao)
))) 

(rf/reg-event-db 
  :ganhou
  (fn [db [_ nova-direcao]]   
    (assoc db 
      :feedback "Ganhou!"
))) 

(rf/reg-event-db 
  :perdeu
  (fn [db [_ nova-direcao]]   
    (assoc db 
      :feedback "Perdeu!"
))) 




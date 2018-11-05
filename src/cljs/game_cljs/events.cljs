(ns game-cljs.events
  (:require
   [re-frame.core :as rf]
   [game-cljs.db :as db]
   ))

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
      :maca (db/proximo-estado-maca (:snake db) (:maca db)))))

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

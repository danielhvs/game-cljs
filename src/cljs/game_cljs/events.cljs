(ns game-cljs.events
  (:require
   [re-frame.core :as rf]
   [game-cljs.db :as db]
   ))

(rf/reg-event-db
 ::initialize-db
 (fn [_ [_ [s m]]]
   {:estado {:snake s
             :maca m}
    :feedback {:texto "Jogando"}
    :rec [{:snake s :maca m}]
    :count 0}))

(rf/reg-event-db 
  :timer                  
  (fn [db [_ new-time]]   
    (assoc db :estado
           (assoc (:estado db) 
             :snake (db/proximo-estado (:snake (:estado db)) (:maca (:estado db)))
             :maca (db/proximo-estado-maca (:snake (:estado db)) (:maca (:estado db)))))))

(rf/reg-event-db 
 :count
 (fn [db [_ f]]   
   (assoc db 
     :feedback (assoc (:feedback db) :count (str "count = " (:count db)))
     :count (let [proximo (f (:count db))
                  tam-rec (count (:rec db))] 
              (if (>= proximo tam-rec) 
                0
                (if (< proximo 0)
                  (dec tam-rec)   
                  proximo))))))

(rf/reg-event-db 
  :historia
  (fn [db [_ new-time]]   
    (assoc db 
      :rec (if (= (:estado db) (last (:rec db))) 
                  (:rec db) 
                  (conj (:rec db) (:estado db)))
)))

(rf/reg-event-db 
  :muda-direcao
  (fn [db [_ nova-direcao]]   
    (assoc-in db [:estado :snake] (db/vira (:snake (:estado db)) nova-direcao)
))) 

(rf/reg-event-db 
  :ganhou
  (fn [db [_ nova-direcao]]   
    (assoc db 
      :feedback (assoc (:feedback db) :texto "Ganhou!")
))) 

(rf/reg-event-db 
  :perdeu
  (fn [db [_ nova-direcao]]   
    (assoc db 
      :feedback (assoc (:feedback db) :texto "Perdeu!")
))) 

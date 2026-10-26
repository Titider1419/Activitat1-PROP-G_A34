(define (domain snake)
    (:requirements :strips :equality :typing :conditional-effects
    )
    (:types TCASELLA TAGENT)
    (:predicates
        (EsParet ?casella - TCASELLA)
        (EstaBuida ?casella - TCASELLA)
        (Veina ?casella ?casella - TCASELLA)
        (EstaAgent ?agent - TAGENT ?casella - TCASELLA)
        (EsSortida ?agent - TAGENT ?casella - TCASELLA)
        (HaSortit ?agent)
    )
(:action Moure
        :parameters (?agent - TAGENT ?deCasella ?aCasella - TCASELLA)
        :precondition (and
                            (or 
                                (EstaBuida ?aCasella)
                                (EsSortida ?agent ?aCasella)
                            )
                            (EstaAgent ?agent ?deCasella)
                            (or
                                (Veina ?aCasella ?deCasella)
                                (Veina ?deCasella ?aCasella)
                            )
                            (not (EsParet ?aCasella))
                    )
        :effect (and
            (EsParet ?deCasella)
            (EstaAgent ?agent ?aCasella)
            (not (EstaAgent ?agent ?deCasella))
            (when (EstaBuida ?aCasella) (not (EstaBuida ?aCasella)))
            (when (EsSortida ?agent ?aCasella) (HaSortit ?agent))
        )
    )
)
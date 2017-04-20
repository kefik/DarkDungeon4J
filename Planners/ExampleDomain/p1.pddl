(define (problem p1) 
(:domain DarkDungeon)

(:objects r1 r2 r3 r4 - room
          s - sword)

(:init

   (alive)
   (connected r1 r2)
   (connected r2 r1)
   (connected r2 r3)
   (connected r3 r2)
   (connected r3 r4)
   (connected r4 r3)

   (monster_at r2)
   (trap_at r3)
   (sword_at r1)

   (hero_at r1)
)

(:goal (and (alive)(hero_at r4)))

)

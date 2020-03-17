To copy testrepo

cp -ru com.github.jamesarthurholland ~/.alfalfa/repository/

Issues:
1. com/github/jamesarthurholland/alfalfa/Alfalfa.java:60 Isn't filtering alfalfafiles properly
2. com/github/jamesarthurholland/alfalfa/Alfalfa.java:66 We want to copy directories as well.
We also want to have some sort of name swap option in the alfalfa file, because 
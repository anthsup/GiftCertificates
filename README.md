Task 0:
For this task you should create two separate branches git_0 and git_1 in your remote repository and
local tracked branches with the same names.
Show the following steps to your mentor during a demo:
1. git_0: Add and commit 1.txt file with 10 lines.
2. git_0: Add and commit 2.txt file with 10 lines.
3. Merge branch git_0 to git_1
4. git_1: Update and commit any two lines in 2.txt.
5. git_0: Update and commit the same 2 lines with the different info in 2.txt
6. Merge branch git_1 to git_0, resolve conflict. Left all (4) modified lines. Commit.
7. git_0: Update and commit 1.txt file, modify two lines.
8. git_0: Update and commit 1.txt file, modify another two lines.
9. Transfer changes of commit from Step 7 only to git_1, using format patch.
10. Transfer changes of commit from Step 8 only to git_1, using cherrypick command.
11. git_1: Concatenate the last two commits using reset + commit commands.
12. git_1: Change date, author and message of the last commit and add non-empty 3.txt file to it.
13. git_1: Create a new commit that reverts changes of the last one.
14. git_1: Update and commit 3.txt file.
15. git_1: Run command that removes all changes of the last two commits.
16. Synchronize git_0 and git_1 with a remote repository.
17. Clone your project to another folder.
18. folder2: git_0: Change two lines in 1.txt. Commit + Push.
19. folder1: git_0: Change another two lines in 1.txt.
20. folder1: git_0:
a. Change another line in 1.txt (not the same as in 18, 19).
b. Merge changes from Step 18 (pull) without committing changes from Step 19 and any
additional commits.
c. Push.
d. Return local state of Step 19. (stash)

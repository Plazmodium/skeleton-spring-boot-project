Task Checklist Working Guidelines

These guidelines ensure the checklist in `docs/tasks.md` stays accurate, traceable, and helpful during development.

1. Marking Progress
- Mark tasks as completed by changing `[ ]` to `[x]`.
- Only mark a task `[x]` when all of its implications are done (code, config, docs, and basic tests for that item).

2. Phases
- Keep existing phases and their order intact (Setup → Persistence → API → Docs → Security → Observability → CI/CD → i18n → Testing → Release).
- You may add new tasks under the most relevant phase. Do not delete phases; deprecate tasks instead by appending “(deprecated)” and a short rationale.

3. Traceability Links
- Every task must reference:
  - A plan item ID from `docs/plan.md` (e.g., `PLAN-3.1`).
  - One or more requirement IDs from `docs/requirements.md` (by number, e.g., `Reqs: 1, 3`).
- When adding or modifying a task, ensure these links are present and accurate.
- It is very easy to forget to update links, so it is recommended to add them as part of the PR description.
- Links are to be versioned with tasks to ensure they remain valid even after refactoring—to create a comprehensive changelog.

4. Consistent Formatting
- Use the exact checklist format: `N. [ ] Task description. (Plan: PLAN-x.y; Reqs: a[, b])`
- Keep numbering sequential within each phase.
- Prefer concise, actionable task descriptions.

5. Evolving Scope
- If scope grows, add new tasks rather than inflating existing ones.
- If a task becomes blocked, add a short note after the description: `— Blocked: reason` (do not change the formatting before the parentheses).
- Tasks are to be versioned to ensure they remain valid even after refactoring—to create a comprehensive changelog.

6. Review & Sync
- After updating tasks, skim `docs/plan.md` and `docs/requirements.md` to confirm links remain correct.
- If `docs/plan.md` changes (e.g., new PLAN IDs), update affected tasks promptly.

7. Quality Gates
- Build the project from scratch and run all tests before marking a task complete.
- If the build fails, mark the task as incomplete, identify and fix the issue.
- Do not check a task `[x]` without at least making sure the solution builds and with minimal tests (unit/integration as applicable) and run instructions if user‑facing.
- Errors and edge cases mentioned in `docs/requirements.md` must be covered in associated tasks before marking complete.

8. PR Hygiene
- Reference the task numbers and PLAN/Req IDs in commit messages and pull requests.
- Keep changes small and focused per task to maintain clarity and revertability.

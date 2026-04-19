#!/bin/bash

# Check if commit message was provided
if [ -z "$1" ]; then
  echo "Usage: ./git-quick-commit.sh \"Your commit message\""
  exit 1
fi

# Stage all changes
git add .

# Commit with the provided message
git commit -m "$1"

# Push to main branch
git push origin main

# Save the script file.

# Make it executable:

# bash run --> chmod +x git-quick-commit.sh
# Run it with your commit message as a parameter:

# bash run --> ./git-quick-commit.sh "Your commit message"
# ./git-quick-commit.sh "Fix eslint and jest config"
# If you want to push to a different branch, just edit the script replacing main with your branch name
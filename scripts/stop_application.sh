#!/bin/bash

sudo systemctl stop ajoukino.service
sleep 10
if systemctl is-active --quiet ajoukino.service; then
  sudo systemctl kill ajoukino.service
fi